package itmo.app.shared.fieldschema;

import itmo.app.shared.Utils.Validator;
import itmo.app.shared.exceptions.ParsingException;
import itmo.app.shared.exceptions.ValidationException;
import java.util.List;

public interface FieldSchema<T, Self extends FieldSchema<T, Self>> extends Validator<T> {
    public T parse(String input) throws ParsingException;

    public List<Validator<T>> validators();

    public Self refine(Validator<T> validator);

    public default T validate(T value) throws ValidationException {
        try {
            for (var validator : this.validators()) {
                validator.validate(value);
            }
            return value;
        } catch (NullPointerException err) {
            return null;
        }
    }

    // TODO: error translation
    public default T fromString(String input, String fieldname) throws ParsingException {
        try {
            T parsed = this.parse(input);
            return this.validate(parsed);
        } catch (ValidationException | ParsingException err) {
            throw new ParsingException(fieldname + ": " + err.getMessage());
        }
    }

    public default Self nonnull() {
        return this.refine(Validator.from(value -> value != null, "value is required"));
    }

    public default Self notequals(T neqvalue) {
        return this.refine(
                Validator.from(
                    value -> !value.equals(neqvalue),
                    "value can not be equal to " + neqvalue
                )
            );
    }

    public default Self mustequal(T eqvalue) {
        return this.refine(
                Validator.from(
                    value -> value.equals(eqvalue),
                    "value must be equal to " + eqvalue
                )
            );
    }

    public static FieldSchemaLocalDate localdate() {
        return new FieldSchemaLocalDate();
    }

    public static <TEnum extends Enum<TEnum>> FieldSchemaEnum<TEnum> enumeration(
        Class<TEnum> enumClass
    ) {
        return new FieldSchemaEnum<TEnum>(enumClass);
    }

    public static FieldSchemaString str() {
        return new FieldSchemaString();
    }

    public static FieldSchemaNum<Integer> integer() {
        return new FieldSchemaNum<>(Integer::parseInt);
    }

    public static FieldSchemaNum<Long> longint() {
        return new FieldSchemaNum<>(Long::parseLong);
    }

    public static FieldSchemaNum<Float> floating() {
        return new FieldSchemaNum<>(Float::parseFloat);
    }

    public static FieldSchemaNum<Double> doublef() {
        return new FieldSchemaNum<>(Double::parseDouble);
    }
}
