package com.kirillr.strictmodehelper;

import android.os.strictmode.Violation;
import android.support.annotation.NonNull;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class ViolationCompat extends Throwable {

    @NonNull
    private final Violation violation;

    ViolationCompat(@NonNull Violation violation) {
        this.violation = violation;
    }

    @Override
    public String getMessage() {
        return violation.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return violation.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return violation.getCause();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return violation.initCause(cause);
    }

    @Override
    public String toString() {
        return violation.toString();
    }

    @Override
    public void printStackTrace() {
        violation.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        violation.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        violation.printStackTrace(s);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return violation.fillInStackTrace();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return violation.getStackTrace();
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        violation.setStackTrace(stackTrace);
    }

    @Override
    public int hashCode() {
        return violation.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ViolationCompat that = (ViolationCompat) o;
        return violation.equals(that.violation);
    }

    @NonNull
    public Violation getViolation() {
        return violation;
    }
}
