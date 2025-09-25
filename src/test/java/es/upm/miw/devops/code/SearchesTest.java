package es.upm.miw.devops.code;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SearchesTest {

    private final Searches searches = new Searches();

    @Test
    void testFindUserFamilyNameByUserNameDistinct() {
        assertThat(new Searches().findUserFamilyNameByUserNameDistinct("Paula").toList())
                .containsExactly("Torres");
    }

    @Test
    void testFindFractionNumeratorByUserFamilyName() {
        List<Integer> result = searches.findFractionNumeratorByUserFamilyName("Blanco")
                .toList();

        assertThat(result)
                .hasSize(7)
                .containsExactly(2, -1, 2, 4, 0, 0, 0);
    }

    @Test
    void testFindUserFamilyNameByFractionDenominator() {
        List<String> result = searches.findUserFamilyNameByFractionDenominator(1)
                .toList();

        assertThat(result)
                .hasSize(4)
                .containsExactlyInAnyOrder("Fernandez", "Blanco", "Blanco", "Torres");
    }

    @Test
    void testFindUserFamilyNameInitialByAnyProperFraction() {
        List<String> result = searches.findUserFamilyNameInitialByAnyProperFraction()
                .toList();

        assertThat(result)
                .hasSize(4)
                .containsExactlyInAnyOrder("F", "B", "L", "B");
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

    @Test
    void testFindUserIdByAnyProperFraction() {
        assertThat(searches.findUserIdByAnyProperFraction().toList())
                .isEmpty();
    }

    @Test
    void testFindUserIdByAllProperFraction() {
        assertThat(searches.findUserIdByAllProperFraction().toList())
                .isEmpty();
    }

    @Test
    void testFindDecimalImproperFractionByUserName() {
        assertThat(searches.findDecimalImproperFractionByUserName("user1").toList())
                .isEmpty();
    }

    @Test
    void testFindFirstProperFractionByUserId() {
        assertThat(searches.findFirstProperFractionByUserId("u1"))
                .isNull();
    }

    @Test
    void testFindFirstFractionDivisionByUserId() {
        assertThat(searches.findFirstFractionDivisionByUserId("u1"))
                .isNull();
    }

    @Test
    void testFindUserFamilyNameByImproperFraction() {
        assertThat(searches.findUserFamilyNameByImproperFraction().toList())
                .isEmpty();
    }

    @Test
    void testFindUserFamilyNameByAllNegativeSignFractionDistinct() {
        assertThat(searches.findUserFamilyNameByAllNegativeSignFractionDistinct().toList())
                .isEmpty();
    }

    @Test
    void testFindDecimalFractionByUserName() {
        assertThat(searches.findDecimalFractionByUserName("user1").toList())
                .isEmpty();
    }

    @Test
    void testFindFirstFractionSubtractionByUserName() {
        assertThat(searches.findFirstFractionSubtractionByUserName("user1"))
                .isNull();
    }

    @Test
    void testFindUserNameByAnyImproperFraction() {
        assertThat(searches.findUserNameByAnyImproperFraction().toList())
                .isEmpty();
    }

    void testFindUserFamilyNameByAllSignFractionDistinct() {
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

    @Test
    void testFindUserFamilyNameBySomeImproperFraction() {
        List<String> result = searches.findUserFamilyNameBySomeImproperFraction()
                .toList();

        assertThat(result).isNotEmpty();
        assertThat(result).containsAnyOf("Fernandez", "Blanco");

        List<String> emptyResult = searches.findUserFamilyNameBySomeImproperFraction()
                .filter(name -> name.equals("Inexistente"))
                .toList();

        assertThat(emptyResult).isEmpty();
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