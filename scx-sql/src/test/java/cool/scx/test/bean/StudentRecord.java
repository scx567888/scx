package cool.scx.test.bean;

public record StudentRecord(String name, Integer age, Boolean sex) {
    public StudentRecord(String name, Integer age) {
        this(name, age, false);
    }

    public StudentRecord(String name, Boolean sex, Integer age) {
        this(name, age, false);
    }
}
