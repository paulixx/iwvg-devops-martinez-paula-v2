package es.upm.miw.devops.code;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchesTest {

    private final Searches searches = new Searches();

    @Test
    void testFindUserFamilyNameByUserNameDistinct() {
        assertThat(new Searches().findUserFamilyNameByUserNameDistinct("Paula").toList())
                .containsExactly("Torres");
    }

    @Test
    void testFindUserFractionNumeratorByFamilyName() {
        assertThat(new Searches().findFractionNumeratorByUserFamilyName("Torres").toList())
                .containsExactly(2, 4, 0, 1, 1);
    }

    @Test
    void testFindFamilyNameByFractionDenominator() {
        assertThat(new Searches().findUserFamilyNameByFractionDenominator(2).toList())
                .containsExactly("López", "Torres");
    }

    void testFindUserIdByAnyProperFraction() {
    }

    void testFindUserNameByAnyImproperFraction() {
    }

    void testFindUserFamilyNameByAllSignFractionDistinct() {
    }

    void testFindDecimalFractionByUserName() {
    }

    void testFindDecimalFractionBySignFraction() {
    }

    @Test
    void testFindFractionAdditionByUserId() {
        Fraction result = searches.findFractionAdditionByUserId("1"); // id existente
        assertThat(result).isNotNull();
        assertThat(result.decimal()).isGreaterThan(0);

        Fraction resultInvalid = searches.findFractionAdditionByUserId("999"); // id inexistente
        assertThat(resultInvalid).isNotNull();
        assertThat(resultInvalid.decimal()).isEqualTo(0.0);
    }

    @Test
    void testFindFractionSubtractionByUserName() {
        Fraction result = searches.findFractionSubtractionByUserName("Ana"); // nombre existente
        assertThat(result).isNotNull();

        Fraction resultInvalid = searches.findFractionSubtractionByUserName("Desconocido");
        assertThat(resultInvalid).isNull();
    }

    @Test
    void testFindFractionMultiplicationByUserFamilyName() {
        Fraction result = searches.findFractionMultiplicationByUserFamilyName("García"); // apellido existente
        assertThat(result).isNotNull();
        assertThat(result.decimal()).isNotZero();

        Fraction resultInvalid = searches.findFractionMultiplicationByUserFamilyName("Inexistente");
        assertThat(resultInvalid).isNotNull();
        assertThat(resultInvalid.decimal()).isEqualTo(1.0);
    }


    //Did it in before version
    @Test
    void testFindHighestFraction() {
        Fraction highest = searches.findHighestFraction();

        assertThat(highest).isNotNull();

        double maxValue = new UsersDatabase().findAll()
                .flatMap(u -> u.getFractions().stream())
                .filter(f -> f != null && f.getDenominator() != 0)
                .mapToDouble(Fraction::decimal)
                .max()
                .orElseThrow();

        assertThat(highest.decimal()).isEqualTo(maxValue);
    }

    //Did it in before version
    @Test
    void testFindUserNameBySomeImproperFraction() {
        List<String> result = searches.findUserNameBySomeImproperFraction().toList();

        assertThat(result).isNotEmpty();

        result.forEach(name -> {
            boolean hasImproper = new UsersDatabase().findAll()
                    .filter(u -> u.getName().equals(name))
                    .flatMap(u -> u.getFractions().stream())
                    .filter(f -> f != null && f.getDenominator() != 0)
                    .anyMatch(Fraction::isImproper);

            assertThat(hasImproper)
                    .as("Usuario %s debería tener al menos una fracción impropia", name)
                    .isTrue();
        });
    }

    //Did it in before version
    @Test
    void testFindFirstDecimalFractionByUserName() {
        String targetName = "user1";
        Double firstDecimal = searches.findFirstDecimalFractionByUserName(targetName);

        Double expected = new UsersDatabase().findAll()
                .filter(u -> u.getName().equals(targetName))
                .flatMap(u -> u.getFractions().stream())
                .filter(f -> f != null && f.getDenominator() != 0)
                .map(Fraction::decimal)
                .findFirst()
                .orElse(null);

        assertThat(firstDecimal).isEqualTo(expected);
    }

    //Did it in before version
    @Test
    void testFindDecimalFractionByNegativeSignFraction() {
        List<Double> result = searches.findDecimalFractionByNegativeSignFraction().toList();

        assertThat(result).allSatisfy(value -> assertThat(value).isLessThan(0));

        List<Double> expected = new UsersDatabase().findAll()
                .flatMap(u -> u.getFractions().stream())
                .filter(f -> f != null && f.getDenominator() != 0)
                .filter(f -> f.getNumerator() * f.getDenominator() < 0)
                .map(Fraction::decimal)
                .toList();

        assertThat(result).containsExactlyElementsOf(expected);
    }
}