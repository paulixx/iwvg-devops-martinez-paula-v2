package es.upm.miw.devops.code;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.Comparator;

public class Searches {

    public Stream<String> findUserFamilyNameByUserNameDistinct(String userName) {
        return new UsersDatabase().findAll()
                .filter(user -> userName.equals(user.getName()))
                .map(User::getFamilyName)
                .distinct();
    }

    public Stream<Integer> findFractionNumeratorByUserFamilyName(String userFamilyName) {
        return new UsersDatabase().findAll()
                .filter(user -> userFamilyName.equals(user.getFamilyName()))
                .flatMap(user -> user.getFractions().stream()
                        .filter(Objects::nonNull)
                )
                .map(Fraction::getNumerator);
    }

    public Stream<String> findUserFamilyNameByFractionDenominator(int fractionDenominator) {
        return new UsersDatabase().findAll()
                .filter(user -> user.getFractions().stream()
                        .anyMatch(fraction -> fractionDenominator == fraction.getDenominator()))
                .map(User::getFamilyName);
    }

    public Stream<String> findUserFamilyNameInitialByAnyProperFraction() {
        return Stream.empty();
    }

    public Stream<String> findUserIdByAnyProperFraction() {
        return Stream.empty();
    }

    public Fraction findFractionMultiplicationByUserFamilyName(String familyName) {
        return null;
    }

    public Fraction findFirstFractionDivisionByUserId(String id) {
        return null;
    }

    //Did it in before version
    public Double findFirstDecimalFractionByUserName(String name) {
        return new UsersDatabase().findAll()
                .filter(user -> name.equals(user.getName()))
                .flatMap(user -> user.getFractions().stream())
                .filter(f -> f != null && f.getDenominator() != 0)
                .map(Fraction::decimal)
                .findFirst()
                .orElse(null);
    }


    public Stream<String> findUserIdByAllProperFraction() {
        return Stream.empty();
    }

    public Stream<Double> findDecimalImproperFractionByUserName(String name) {
        return Stream.empty();
    }

    public Fraction findFirstProperFractionByUserId(String id) {
        return null;
    }

    public Stream<String> findUserFamilyNameByImproperFraction() {
        return Stream.empty();
    }

    //Did it in before version
    public Fraction findHighestFraction() {
        return new UsersDatabase().findAll()
                .flatMap(user -> user.getFractions().stream())
                .filter(Objects::nonNull)
                .filter(f -> f.getDenominator() != 0) // evitamos división por 0
                .max(Comparator.comparingDouble(Fraction::decimal))
                .orElse(null);
    }


    public Stream<String> findUserNameByAnyImproperFraction() {
        return Stream.empty();
    }

    //Did it in before version
    public Stream<String> findUserNameBySomeImproperFraction() {
        return new UsersDatabase().findAll()
                .filter(user -> user.getFractions().stream()
                        .filter(Objects::nonNull)
                        .filter(f -> f.getDenominator() != 0) // Evitar inválidas
                        .anyMatch(Fraction::isImproper))
                .map(User::getName);
    }

    public Stream<String> findUserFamilyNameByAllNegativeSignFractionDistinct() {
        return Stream.empty();
    }

    public Stream<Double> findDecimalFractionByUserName(String name) {
        return Stream.empty();
    }

    //Did it in before version
    public Stream<Double> findDecimalFractionByNegativeSignFraction() {
        return new UsersDatabase().findAll()
                .flatMap(user -> user.getFractions().stream())
                .filter(f -> f != null && f.getDenominator() != 0)
                .filter(f -> f.getNumerator() * f.getDenominator() < 0) // fracción con signo negativo
                .map(Fraction::decimal);
    }

    public Fraction findFractionAdditionByUserId(String id) {
        return new UsersDatabase().findAll()
                .filter(user -> id.equals(user.getId()))
                .flatMap(user -> user.getFractions().stream())
                .filter(f -> f != null && f.getDenominator() != 0)
                .reduce(new Fraction(0, 1), Fraction::add);
    }

    public Fraction findFirstFractionSubtractionByUserName(String name) {
        return null;
    }

    public Fraction findFractionSubtractionByUserName(String name) {
        return new UsersDatabase().findAll()
                .filter(user -> name.equals(user.getName()))
                .flatMap(user -> user.getFractions().stream())
                .filter(f -> f != null && f.getDenominator() != 0)
                .reduce((f1, f2) -> f1.add(new Fraction(-f2.getNumerator(), f2.getDenominator())))
                .orElse(null);
    }
}