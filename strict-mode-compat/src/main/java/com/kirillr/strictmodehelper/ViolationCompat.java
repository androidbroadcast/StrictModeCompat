package com.kirillr.strictmodehelper;

import android.os.strictmode.Violation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class ViolationCompat extends Throwable {

    @NonNull
    private final Violation violation;

    ViolationCompat(@NonNull Violation violation) {
        this.violation = violation;
    }

    @Nullable
    @Override
    public String getMessage() {
        return violation.getMessage();
    }

    @Nullable
    @Override
    public String getLocalizedMessage() {
        return violation.getLocalizedMessage();
    }

    @Nullable
    @Override
    public Throwable getCause() {
        return violation.getCause();
    }

    @NonNull
    @Override
    public Throwable initCause(@Nullable Throwable cause) {
        return violation.initCause(cause);
    }

    @NonNull
    @Override
    public String toString() {
        return violation.toString();
    }

    @Override
    public void printStackTrace() {
        violation.printStackTrace();
    }

    @Override
    public void printStackTrace(@NonNull PrintStream s) {
        violation.printStackTrace(s);
    }

    @Override
    public void printStackTrace(@NonNull PrintWriter s) {
        violation.printStackTrace(s);
    }

    @NonNull
    @Override
    public Throwable fillInStackTrace() {
        return violation.fillInStackTrace();
    }

    @NonNull
    @Override
    public StackTraceElement[] getStackTrace() {
        return violation.getStackTrace();
    }

    @Override
    public void setStackTrace(@NonNull StackTraceElement[] stackTrace) {
        violation.setStackTrace(stackTrace);
    }

    @Override
    public int hashCode() {
        return violation.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
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
