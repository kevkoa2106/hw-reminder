package com.betterhomework.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Homework {
    private String name;
    private String subject;
    private LocalDateTime dueDate;  // matches getter
    private boolean completed;

    public Homework(String name, String subject, LocalDateTime dueDate, boolean completed) {
        this.name = name;
        this.subject = subject;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Homework)) return false;
        Homework homework = (Homework) o;
        return completed == homework.completed
                && Objects.equals(name, homework.name)
                && Objects.equals(subject, homework.subject)
                && Objects.equals(dueDate, homework.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subject, dueDate, completed);
    }
}