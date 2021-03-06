/*
 * Copyright 2017-2019 Kirill Rozov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kirillr.strictmodehelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.StrictMode;
import android.os.strictmode.Violation;

import java.io.Closeable;
import java.util.Locale;
import java.util.concurrent.Executor;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings("ALL")
public final class StrictModeCompat {

    private StrictModeCompat() {
    }

    /**
     * A convenience wrapper that takes the current
     * {@link ThreadPolicy} from {@link #getThreadPolicy}, modifies it
     * to permit disk reads, and sets the new policy
     * with {@link #setThreadPolicy}, returning the old policy so you
     * can restore it at the end of a block.
     *
     * @return the old policy, to be passed to setThreadPolicy to restore the policy.
     */
    @NonNull
    public static StrictMode.ThreadPolicy allowThreadDiskReads() {
        return StrictMode.allowThreadDiskReads();
    }

    /**
     * A convenience wrapper that takes the current
     * {@link ThreadPolicy} from {@link #getThreadPolicy}, modifies it
     * to permit both disk reads &amp; writes, and sets the new policy
     * with {@link #setThreadPolicy}, returning the old policy so you
     * can restore it at the end of a block.
     *
     * @return the old policy, to be passed to {@link #setThreadPolicy} to
     * restore the policy at the end of a block
     */
    @NonNull
    public static StrictMode.ThreadPolicy allowThreadDiskWrites() {
        return StrictMode.allowThreadDiskWrites();
    }

    /**
     * Enable the recommended StrictMode defaults, with violations just being logged.
     * <p>
     * <p>This catches disk and network access on the main thread, as
     * well as leaked SQLite cursors and unclosed resources.  This is
     * simply a wrapper around {@link #setVmPolicy} and {@link
     * #setThreadPolicy}.
     */
    public static void enableDefaults() {
        StrictMode.enableDefaults();
    }

    /**
     * Returns the current thread's policy.
     *
     * @see StrictMode#getThreadPolicy()
     */
    @NonNull
    public static StrictMode.ThreadPolicy getThreadPolicy() {
        return StrictMode.getThreadPolicy();
    }

    /**
     * Gets the current VM policy.
     */
    @NonNull
    public static StrictMode.VmPolicy getVmPolicy() {
        return StrictMode.getVmPolicy();
    }

    /**
     * For code to note that it's slow.  This is a no-op unless the
     * current thread's {@link android.os.StrictMode.ThreadPolicy} has
     * {@link android.os.StrictMode.ThreadPolicy.Builder#detectCustomSlowCalls}
     * enabled.
     *
     * @param name Short string for the exception stack trace that's
     *             built if when this fires.
     * @see StrictModeCompat#noteSlowCall(String, Object...)
     * @see StrictModeCompat#noteSlowCall(Locale, String, Object...)
     */
    public static void noteSlowCall(@NonNull String name) {
        StrictMode.noteSlowCall(name);
    }

    /**
     * For code to note that it's slow.  This is a no-op unless the
     * current thread's {@link android.os.StrictMode.ThreadPolicy} has
     * {@link android.os.StrictMode.ThreadPolicy.Builder#detectCustomSlowCalls}
     * enabled.
     *
     * @param message Short formatting string for the exception stack trace that's
     *                built if when this fires.
     * @param args    Arguments referenced by the format specifiers in the format string
     * @see StrictModeCompat#noteSlowCall(String)
     * @see StrictModeCompat#noteSlowCall(Locale, String, Object...)
     */
    public static void noteSlowCall(@NonNull String message, @NonNull Object... args) {
        StrictMode.noteSlowCall(String.format(message, args));
    }

    /**
     * For code to note that it's slow.  This is a no-op unless the
     * current thread's {@link android.os.StrictMode.ThreadPolicy} has
     * {@link android.os.StrictMode.ThreadPolicy.Builder#detectCustomSlowCalls}
     * enabled.
     *
     * @param locale  The locale to apply during formatting
     * @param message Short formatting string for the exception stack trace that's
     *                built if when this fires.
     * @param args    Arguments referenced by the format specifiers in the format string
     * @see StrictModeCompat#noteSlowCall(String)
     * @see StrictModeCompat#noteSlowCall(String, Object...)
     */
    public static void noteSlowCall(@Nullable Locale locale, @NonNull String message, @NonNull Object... args) {
        if (locale == null) {
            StrictMode.noteSlowCall(String.format(message, args));
        } else {
            StrictMode.noteSlowCall(String.format(locale, message, args));
        }
    }

    /**
     * Sets the policy for what actions on the current thread should
     * be detected, as well as the penalty if such actions occur.
     * <p>
     * <p>Internally this sets a thread-local variable which is
     * propagated across cross-process IPC calls, meaning you can
     * catch violations when a system service or another process
     * accesses the disk or network on your behalf.
     *
     * @param policy the policy to put into place
     */
    public static void setThreadPolicy(@NonNull StrictMode.ThreadPolicy policy) {
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * Sets the policy for what actions in the VM process (on any
     * thread) should be detected, as well as the penalty if such
     * actions occur.
     *
     * @param policy the policy to put into place
     */
    public static void setVmPolicy(@NonNull StrictMode.VmPolicy policy) {
        StrictMode.setVmPolicy(policy);
    }

    /**
     * Set Thread and VM policies in one method.
     *
     * @param threadPolicy the thread policy to put into place
     * @param vmPolicy     the vm policy to put into place
     */
    public static void setPolicies(
            @NonNull StrictMode.ThreadPolicy threadPolicy,
            @NonNull StrictMode.VmPolicy vmPolicy
    ) {
        setThreadPolicy(threadPolicy);
        setVmPolicy(vmPolicy);
    }

    /**
     * When {@link StrictMode.VmPolicy.Builder#penaltyListener(Executor, StrictMode.OnVmViolationListener)} is enabled,
     * the listener is called on the provided executor when a VM violation occurs.
     */
    public interface OnVmViolationListener {

        /**
         * Called on a VM policy violation.
         */
        @TargetApi(Build.VERSION_CODES.O)
        void onVmViolation(@NonNull Violation violation);
    }

    /**
     * When {@link StrictMode.ThreadPolicy.Builder#penaltyListener(Executor, StrictMode.OnThreadViolationListener)} is enabled,
     * the listener is called on the provided executor when a Thread violation occurs.
     */
    public interface OnThreadViolationListener {

        /**
         * Called on a thread policy violation.
         */
        @TargetApi(Build.VERSION_CODES.O)
        void onThreadViolation(@NonNull Violation violation);
    }

    public static final class ThreadPolicy {

        private ThreadPolicy() {
        }

        public static final class Builder {

            @NonNull
            private final BuilderImpl mBuilder;

            public Builder() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mBuilder = new V28BuilderImpl();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mBuilder = new V26BuilderImpl();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBuilder = new V23BuilderImpl();
                } else {
                    mBuilder = new V14BuilderImpl();
                }
            }

            public Builder(@NonNull StrictMode.ThreadPolicy policy) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mBuilder = new V28BuilderImpl(policy);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mBuilder = new V26BuilderImpl(policy);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBuilder = new V23BuilderImpl(policy);
                } else {
                    mBuilder = new V14BuilderImpl(policy);
                }
            }

            /**
             * Construct the ThreadPolicy instance.
             * <p>
             * <p>Note: if no penalties are enabled before calling
             * <code>build</code>, {@link #penaltyLog} is implicitly
             * set.
             */
            public StrictMode.ThreadPolicy build() {
                return mBuilder.build();
            }

            /**
             * Detect everything that's potentially suspect.
             */
            public Builder detectAll() {
                mBuilder.detectAll();
                return this;
            }

            /**
             * Enable detection of slow calls.
             */
            public Builder detectCustomSlowCalls() {
                mBuilder.detectCustomSlowCalls();
                return this;
            }

            /**
             * Enable detection of disk reads.
             */
            public Builder detectDiskReads() {
                mBuilder.detectDiskReads();
                return this;
            }

            /**
             * Enable detection of disk writes.
             */
            public Builder detectDiskWrites() {
                mBuilder.detectDiskWrites();
                return this;
            }

            /**
             * Enable detection of network operations.
             */
            public Builder detectNetwork() {
                mBuilder.detectNetwork();
                return this;
            }

            /**
             * Enables detection of mismatches between defined resource types
             * and getter calls.
             * <p>
             * This helps detect accidental type mismatches and potentially
             * expensive type conversions when obtaining typed resources.
             * <p>
             * For example, a strict mode violation would be thrown when
             * calling {@link TypedArray#getInt(int, int)}
             * on an index that contains a String-type resource. If the string
             * value can be parsed as an integer, this method call will return
             * a value without crashing; however, the developer should format
             * the resource as an integer to avoid unnecessary type conversion.
             */
            public Builder detectResourceMismatches() {
                mBuilder.detectResourceMismatches();
                return this;
            }

            /**
             * Crash the whole process on violation.  This penalty runs at
             * the end of all enabled penalties so you'll still get
             * see logging or other violations before the process dies.
             * <p>
             * <p>Unlike {@link #penaltyDeathOnNetwork}, this applies
             * to disk reads, disk writes, and network usage if their
             * corresponding detect flags are set.
             */
            public Builder penaltyDeath() {
                mBuilder.penaltyDeath();
                return this;
            }

            /**
             * Crash the whole process on any network usage.  Unlike
             * {@link #penaltyDeath}, this penalty runs
             * <em>before</em> anything else.  You must still have
             * called {@link #detectNetwork} to enable this.
             * <p>
             * <p>In the Honeycomb or later SDKs, this is on by default.
             */
            public Builder penaltyDeathOnNetwork() {
                mBuilder.penaltyDeathOnNetwork();
                return this;
            }

            /**
             * Show an annoying dialog to the developer on detected
             * violations, rate-limited to be only a little annoying.
             */
            public Builder penaltyDialog() {
                mBuilder.penaltyDialog();
                return this;
            }

            /**
             * Enable detected violations log a stacktrace and timing data
             * to the {@link DropBoxManager DropBox} on policy
             * violation.  Intended mostly for platform integrators doing
             * beta user field data collection.
             */
            public Builder penaltyDropBox() {
                mBuilder.penaltyDropBox();
                return this;
            }

            /**
             * Flash the screen during a violation.
             */
            public Builder penaltyFlashScreen() {
                mBuilder.penaltyFlashScreen();
                return this;
            }

            /**
             * Log detected violations to the system log.
             */
            public Builder penaltyLog() {
                mBuilder.penaltyLog();
                return this;
            }

            /**
             * Disable the detection of everything.
             */
            public Builder permitAll() {
                mBuilder.permitAll();
                return this;
            }

            /**
             * Disable detection of slow calls.
             */
            public Builder permitCustomSlowCalls() {
                mBuilder.permitCustomSlowCalls();
                return this;
            }

            /**
             * Disable detection of disk reads.
             */
            public Builder permitDiskReads() {
                mBuilder.permitDiskReads();
                return this;
            }

            /**
             * Disable detection of disk writes.
             */
            public Builder permitDiskWrites() {
                mBuilder.permitDiskWrites();
                return this;
            }

            /**
             * Disable detection of network operations.
             */
            public Builder permitNetwork() {
                mBuilder.permitNetwork();
                return this;
            }

            /**
             * Disable detection of mismatches between defined resource types
             * and getter calls.
             */
            public Builder permitResourceMismatches() {
                mBuilder.permitResourceMismatches();
                return this;
            }

            /**
             * Detect unbuffered input/output operations.
             * <p>
             * Work on {@link Build.VERSION_CODES#O} and newer.
             */
            public Builder detectUnbufferedIo() {
                mBuilder.detectUnbufferedIo();
                return this;
            }

            /**
             * Disable detection of unbuffered input/output operations.
             * <p>
             * Work on {@link Build.VERSION_CODES#O} and newer.
             */
            public Builder permitUnbufferedIo() {
                mBuilder.permitUnbufferedIo();
                return this;
            }

            /**
             * Call {@link StrictMode.OnThreadViolationListener#onThreadViolation(Violation)}
             * on specified executor every violation.
             * <p>
             * Work on {@link Build.VERSION_CODES#P} and newer.
             *
             * @param executor This value must never be null.
             * @param listener This value must never be null.
             */
            public Builder penaltyListener(
                    @NonNull Executor executor,
                    @NonNull OnThreadViolationListener listener
            ) {
                mBuilder.penaltyListener(executor, listener);
                return this;
            }
        }

        private interface BuilderImpl {

            String CATEGORY = "ThreadPolicy";

            StrictMode.ThreadPolicy build();

            void detectAll();

            void detectCustomSlowCalls();

            void detectDiskReads();

            void detectDiskWrites();

            void detectNetwork();

            void penaltyDeath();

            void penaltyDeathOnNetwork();

            void penaltyDialog();

            void penaltyDropBox();

            void penaltyFlashScreen();

            void penaltyLog();

            void permitAll();

            void permitCustomSlowCalls();

            void permitDiskReads();

            void permitDiskWrites();

            void permitNetwork();

            // Min sdk 23
            void detectResourceMismatches();

            // Min sdk 23
            void permitResourceMismatches();

            // Min sdk 26
            void detectUnbufferedIo();

            // Min sdk 26
            void permitUnbufferedIo();

            // Min sdk 28
            void penaltyListener(@NonNull Executor executor, @NonNull OnThreadViolationListener listener);
        }

        private static class V14BuilderImpl implements BuilderImpl {

            @NonNull
            final StrictMode.ThreadPolicy.Builder builder;

            V14BuilderImpl() {
                this.builder = new StrictMode.ThreadPolicy.Builder();
            }

            V14BuilderImpl(@NonNull StrictMode.ThreadPolicy policy) {
                this.builder = new StrictMode.ThreadPolicy.Builder(policy);
            }

            @Override
            public void detectCustomSlowCalls() {
                builder.detectCustomSlowCalls();
            }

            @Override
            public void penaltyDeathOnNetwork() {
                builder.penaltyDeathOnNetwork();
            }

            @Override
            public void penaltyFlashScreen() {
                builder.penaltyFlashScreen();
            }

            @Override
            public void permitCustomSlowCalls() {
                builder.permitCustomSlowCalls();
            }

            @Override
            public StrictMode.ThreadPolicy build() {
                return builder.build();
            }

            @Override
            public void detectAll() {
                builder.detectAll();
            }

            @Override
            public void detectDiskReads() {
                builder.detectDiskReads();
            }

            @Override
            public void detectDiskWrites() {
                builder.detectDiskWrites();
            }

            @Override
            public void detectNetwork() {
                builder.detectNetwork();
            }

            @Override
            public void penaltyDeath() {
                builder.penaltyDeath();
            }

            @Override
            public void penaltyDialog() {
                builder.penaltyDialog();
            }

            @Override
            public void penaltyDropBox() {
                builder.penaltyDropBox();
            }

            @Override
            public void penaltyLog() {
                builder.penaltyLog();
            }

            @Override
            public void permitAll() {
                builder.permitAll();
            }

            @Override
            public void permitDiskReads() {
                builder.permitDiskReads();
            }

            @Override
            public void permitDiskWrites() {
                builder.permitDiskWrites();
            }

            @Override
            public void permitNetwork() {
                builder.permitNetwork();
            }

            // Min sdk 23
            @Override
            public void detectResourceMismatches() {
                Utils.logUnsupportedFeature(CATEGORY, "Resource mismatches");
            }

            // Min sdk 23
            @Override
            public void permitResourceMismatches() {
                Utils.logUnsupportedFeature(CATEGORY, "Resource mismatches");
            }

            // Min sdk 26
            @Override
            public void detectUnbufferedIo() {
                Utils.logUnsupportedFeature(CATEGORY, "Unbuffered IO");
            }

            // Min sdk 26
            @Override
            public void permitUnbufferedIo() {
                Utils.logUnsupportedFeature(CATEGORY, "Unbuffered IO");
            }

            // Min sdk 28
            @Override
            public void penaltyListener(@NonNull Executor executor, @NonNull OnThreadViolationListener listener) {
                Utils.logUnsupportedFeature(CATEGORY, "Penalty listener");
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        private static class V23BuilderImpl extends V14BuilderImpl {

            V23BuilderImpl() {
            }

            V23BuilderImpl(@NonNull StrictMode.ThreadPolicy policy) {
                super(policy);
            }

            @Override
            public void detectResourceMismatches() {
                builder.detectResourceMismatches();
            }

            @Override
            public void permitResourceMismatches() {
                builder.permitResourceMismatches();
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private static class V26BuilderImpl extends V23BuilderImpl {

            V26BuilderImpl() {
            }

            V26BuilderImpl(@NonNull StrictMode.ThreadPolicy policy) {
                super(policy);
            }

            @Override
            public void detectUnbufferedIo() {
                builder.detectUnbufferedIo();
            }

            @Override
            public void permitUnbufferedIo() {
                builder.permitUnbufferedIo();
            }
        }

        @TargetApi(Build.VERSION_CODES.P)
        private static class V28BuilderImpl extends V26BuilderImpl {

            V28BuilderImpl() {
            }

            V28BuilderImpl(@NonNull StrictMode.ThreadPolicy policy) {
                super(policy);
            }

            @Override
            public void penaltyListener(
                    @NonNull Executor executor,
                    @NonNull final OnThreadViolationListener listener
            ) {
                builder.penaltyListener(
                        executor,
                        new StrictMode.OnThreadViolationListener() {

                            @Override
                            public void onThreadViolation(Violation violation) {
                                listener.onThreadViolation(violation);
                            }
                        }
                );
            }
        }
    }

    public static final class VmPolicy {

        private VmPolicy() {
        }

        public static final class Builder {

            @NonNull
            private final BuilderImpl mBuilder;

            public Builder() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mBuilder = new V29BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mBuilder = new V28BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mBuilder = new V26BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mBuilder = new V24BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBuilder = new V23BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    mBuilder = new V18BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBuilder = new V16BuilderImpl();

                } else {
                    mBuilder = new V14BuilderImpl();
                }
            }

            public Builder(@NonNull StrictMode.VmPolicy policy) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mBuilder = new V29BuilderImpl(policy);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mBuilder = new V28BuilderImpl(policy);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mBuilder = new V26BuilderImpl(policy);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mBuilder = new V24BuilderImpl(policy);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBuilder = new V23BuilderImpl(policy);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    mBuilder = new V18BuilderImpl(policy);

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBuilder = new V16BuilderImpl(policy);

                } else {
                    mBuilder = new V14BuilderImpl(policy);
                }
            }

            public StrictMode.VmPolicy build() {
                return mBuilder.build();
            }

            /**
             * Detect leaks of {@link Activity} subclasses.
             */
            public Builder detectActivityLeaks() {
                mBuilder.detectActivityLeaks();
                return this;
            }

            /**
             * Detect everything that's potentially suspect.
             * <p>
             * <p>In the Honeycomb release this includes leaks of
             * SQLite cursors, Activities, and other closable objects
             * but will likely expand in future releases.
             */
            public Builder detectAll() {
                mBuilder.detectAll();
                return this;
            }

            /**
             * Detect any network traffic from the calling app which is not
             * wrapped in SSL/TLS. This can help you detect places that your app
             * is inadvertently sending cleartext data across the network.
             * <p>
             * Using {@link #penaltyDeath()} or
             * {@link #penaltyDeathOnCleartextNetwork()} will block further
             * traffic on that socket to prevent accidental data leakage, in
             * addition to crashing your process.
             * <p>
             * Using {@link #penaltyDropBox()} will log the raw contents of the
             * packet that triggered the violation.
             * <p>
             * This inspects both IPv4/IPv6 and TCP/UDP network traffic, but it
             * may be subject to false positives, such as when STARTTLS
             * protocols or HTTP proxies are used.
             */
            public Builder detectCleartextNetwork() {
                mBuilder.detectCleartextNetwork();
                return this;
            }

            /**
             * Detect when this application exposes a {@code file://}
             * {@link Uri} to another app.
             * <p>
             * This exposure is discouraged since the receiving app may not have
             * access to the shared path. For example, the receiving app may not
             * have requested the
             * {@link android.Manifest.permission#READ_EXTERNAL_STORAGE} runtime
             * permission, or the platform may be sharing the
             * {@link Uri} across user profile boundaries.
             * <p>
             * Instead, apps should use {@code content://} Uris so the platform
             * can extend temporary permission for the receiving app to access
             * the resource.
             *
             * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
             */
            @SuppressWarnings("JavadocReference")
            public Builder detectFileUriExposure() {
                mBuilder.detectFileUriExposure();
                return this;
            }

            /**
             * Detect when an {@link Closeable} or other
             * object with a explict termination method is finalized
             * without having been closed.
             * <p>
             * <p>You always want to explicitly close such objects to
             * avoid unnecessary resources leaks.
             */
            public Builder detectLeakedClosableObjects() {
                mBuilder.detectLeakedClosableObjects();
                return this;
            }

            /**
             * Detect when a {@link BroadcastReceiver} or
             * {@link ServiceConnection} is leaked during {@link Context}
             * teardown.
             */
            public Builder detectLeakedRegistrationObjects() {
                mBuilder.detectLeakedRegistrationObjects();
                return this;
            }

            /**
             * Detect when an
             * {@link SQLiteCursor} or other
             * SQLite object is finalized without having been closed.
             * <p>
             * <p>You always want to explicitly close your SQLite
             * cursors to avoid unnecessary database contention and
             * temporary memory leaks.
             */
            public Builder detectLeakedSqlLiteObjects() {
                mBuilder.detectLeakedSqlLiteObjects();
                return this;
            }

            /**
             * Crashes the whole process on violation. This penalty runs at the
             * end of all enabled penalties so you'll still get your logging or
             * other violations before the process dies.
             */
            public Builder penaltyDeath() {
                mBuilder.penaltyDeath();
                return this;
            }

            /**
             * Crashes the whole process when cleartext network traffic is
             * detected.
             *
             * @see #detectCleartextNetwork()
             */
            public Builder penaltyDeathOnCleartextNetwork() {
                mBuilder.penaltyDeathOnCleartextNetwork();
                return this;
            }

            /**
             * Crashes the whole process when a {@code file://}
             * {@link Uri} is exposed beyond this app.
             *
             * @see #detectFileUriExposure()
             */
            public Builder penaltyDeathOnFileUriExposure() {
                mBuilder.penaltyDeathOnFileUriExposure();
                return this;
            }

            /**
             * Enable detected violations log a stacktrace and timing data
             * to the {@link DropBoxManager DropBox} on policy
             * violation.  Intended mostly for platform integrators doing
             * beta user field data collection.
             */
            public Builder penaltyDropBox() {
                mBuilder.penaltyDropBox();
                return this;
            }

            /**
             * Log detected violations to the system log.
             */
            public Builder penaltyLog() {
                mBuilder.penaltyLog();
                return this;
            }

            /**
             * Set an upper bound on how many instances of a class can be in memory
             * at once.  Helps to prevent object leaks.
             *
             * @param klass         Class for what apply instances limit
             * @param instanceLimit Max instances count
             */
            public Builder setClassInstanceLimit(@NonNull Class<?> klass,
                                                 @IntRange(from = 0) int instanceLimit) {
                mBuilder.setClassInstanceLimit(klass, instanceLimit);
                return this;
            }

            /**
             * Detect when the calling application sends a content:// Uri to another app
             * without setting {@link Intent#FLAG_GRANT_READ_URI_PERMISSION} or
             * {@link Intent#FLAG_GRANT_WRITE_URI_PERMISSION}.<p>
             * Forgetting to include one or more of these flags
             * when sending an intent is typically an app bug.
             */
            public Builder detectContentUriWithoutPermission() {
                mBuilder.detectContentUriWithoutPermission();
                return this;
            }

            /**
             * Detect any sockets in the calling app which have not been tagged
             * using {@link android.net.TrafficStats}.
             * Tagging sockets can help you investigate network usage inside your app,
             * such as a narrowing down heavy usage to a specific library or component.
             * <p>This currently does not detect sockets created in native code.
             */
            public Builder detectUntaggedSockets() {
                mBuilder.detectUntaggedSockets();
                return this;
            }

            /**
             * Detect reflective usage of APIs that are not part of the public Android SDK.
             * <p>
             * Note that any non-SDK APIs that this processes accesses before this detection is enabled may not be detected.
             * To ensure that all such API accesses are detected, you should apply this policy as early as possible after process creation.
             */
            public Builder detectNonSdkApiUsage() {
                mBuilder.detectNonSdkApiUsage();
                return this;
            }

            /**
             * Call {@link StrictMode.OnVmViolationListener#onVmViolation(Violation)} on every violation.
             *
             * @param executor This value must never be null.
             * @param listener This value must never be null.
             */
            public Builder penaltyListener(@NonNull Executor executor,
                                           @NonNull OnVmViolationListener listener) {
                mBuilder.penaltyListener(executor, listener);
                return this;
            }

            /**
             * Permit reflective usage of APIs that are not part of the public Android SDK.
             * <p>
             * Note that this only affects StrictMode, the underlying runtime may continue
             * to restrict or warn on access to methods that are not part of the public SDK.
             */
            public Builder permitNonSdkApiUsage() {
                mBuilder.permitNonSdkApiUsage();
                return this;
            }

            /**
             * Detect any implicit reliance on Direct Boot automatic filtering of
             * {@link android.content.pm.PackageManager} values. Violations are only triggered
             * when implicit calls are made while the user is locked.
             * <p>
             * Apps becoming Direct Boot aware need to carefully inspect each query site
             * and explicitly decide which combination of flags they want to use:
             *
             * <ul>
             *  <li>PackageManager#MATCH_DIRECT_BOOT_AWARE</li>
             *  <li>PackageManager#MATCH_DIRECT_BOOT_UNAWARE</li>
             *  <li>PackageManager#MATCH_DIRECT_BOOT_AUTO</li>
             * </ul>
             */
            public Builder detectImplicitDirectBoot() {
                mBuilder.detectImplicitDirectBoot();
                return this;
            }

            /**
             * Detect access to filesystem paths stored in credential
             * protected storage areas while the user is locked.
             * <p>
             * When a user is locked, credential protected storage is unavailable,
             * and files stored in these locations appear to not exist, which can result
             * in subtle app bugs if they assume default behaviors or empty states.
             * Instead, apps should store data needed while a user is locked
             * under device protected storage areas.
             */
            public Builder detectCredentialProtectedWhileLocked() {
                mBuilder.detectCredentialProtectedWhileLocked();
                return this;
            }
        }

        private interface BuilderImpl {

            String CATEGORY = "VmPolicy";

            StrictMode.VmPolicy build();

            void detectActivityLeaks();

            void detectAll();

            // Min SDK 23
            void detectCleartextNetwork();

            // Min SDK 18
            void detectFileUriExposure();

            void detectLeakedClosableObjects();

            // Min SDK 16
            void detectLeakedRegistrationObjects();

            void detectLeakedSqlLiteObjects();

            void penaltyDeath();

            // Min SDK 23
            void penaltyDeathOnCleartextNetwork();

            // Min SDK 24
            void penaltyDeathOnFileUriExposure();

            void penaltyDropBox();

            void penaltyLog();

            void setClassInstanceLimit(@NonNull Class<?> klass, @IntRange(from = 0) int instanceLimit);

            // Min SDK 26
            void detectContentUriWithoutPermission();

            // Min SDK 26
            void detectUntaggedSockets();

            // Min SDK 28
            void detectNonSdkApiUsage();

            // Min SDK 28
            void penaltyListener(@NonNull Executor executor, @NonNull OnVmViolationListener listener);

            // Min SDK 28
            void permitNonSdkApiUsage();

            // Min SDK 29
            void detectImplicitDirectBoot();

            // Min SDK 29
            void detectCredentialProtectedWhileLocked();
        }

        private static class V14BuilderImpl implements BuilderImpl {

            @NonNull
            final StrictMode.VmPolicy.Builder mBuilder;

            V14BuilderImpl() {
                mBuilder = new StrictMode.VmPolicy.Builder();
            }

            V14BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                mBuilder = new StrictMode.VmPolicy.Builder(policy);
            }

            @Override
            public StrictMode.VmPolicy build() {
                return mBuilder.build();
            }

            @Override
            public void detectAll() {
                mBuilder.detectAll();
            }

            @Override
            public void detectCleartextNetwork() {
                Utils.logUnsupportedFeature(CATEGORY, "Cleartext network");
            }

            @Override
            public void detectFileUriExposure() {
                Utils.logUnsupportedFeature(CATEGORY, "File uri exposure");
            }

            @Override
            public void detectLeakedSqlLiteObjects() {
                mBuilder.detectLeakedSqlLiteObjects();
            }

            @Override
            public void detectLeakedRegistrationObjects() {
                Utils.logUnsupportedFeature(CATEGORY, "Leaked registration objects");
            }

            @Override
            public void penaltyDeath() {
                mBuilder.penaltyDeath();
            }

            @Override
            public void penaltyDeathOnCleartextNetwork() {
                Utils.logUnsupportedFeature(CATEGORY, "Cleartext network");
            }

            @Override
            public void penaltyDeathOnFileUriExposure() {
                Utils.logUnsupportedFeature(CATEGORY, "Penalty death on file uri exposure");
            }

            @Override
            public void penaltyDropBox() {
                mBuilder.penaltyDropBox();
            }

            @Override
            public void penaltyLog() {
                mBuilder.penaltyLog();
            }

            @Override
            public void detectActivityLeaks() {
                mBuilder.detectActivityLeaks();
            }

            @Override
            public void detectLeakedClosableObjects() {
                mBuilder.detectLeakedClosableObjects();
            }

            @Override
            public void setClassInstanceLimit(@NonNull Class<?> klass, @IntRange(from = 0) int instanceLimit) {
                mBuilder.setClassInstanceLimit(klass, instanceLimit);
            }

            @Override
            public void detectContentUriWithoutPermission() {
                Utils.logUnsupportedFeature(CATEGORY, "Content uri without permission");
            }

            @Override
            public void detectUntaggedSockets() {
                Utils.logUnsupportedFeature(CATEGORY, "Untagged sockets");
            }

            @Override
            public void detectNonSdkApiUsage() {
                Utils.logUnsupportedFeature(CATEGORY, "Non SDK api usage");
            }

            @Override
            public void penaltyListener(@NonNull Executor executor,
                                        @NonNull OnVmViolationListener listener) {
                Utils.logUnsupportedFeature(CATEGORY, "Penalty listener");
            }

            @Override
            public void permitNonSdkApiUsage() {
                Utils.logUnsupportedFeature(CATEGORY, "Non SDK api usage");
            }

            @Override
            public void detectImplicitDirectBoot() {
                Utils.logUnsupportedFeature(CATEGORY, "Implicit Direct Boot");
            }

            @Override
            public void detectCredentialProtectedWhileLocked() {
                Utils.logUnsupportedFeature(CATEGORY, "Credential Protected While Locked");
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private static class V16BuilderImpl extends V14BuilderImpl {

            V16BuilderImpl() {
            }

            V16BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                super(policy);
            }

            @Override
            public void detectLeakedRegistrationObjects() {
                mBuilder.detectLeakedRegistrationObjects();
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        private static class V18BuilderImpl extends V16BuilderImpl {

            V18BuilderImpl() {
            }

            V18BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                super(policy);
            }

            @Override
            public void detectFileUriExposure() {
                mBuilder.detectFileUriExposure();
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        private static class V23BuilderImpl extends V18BuilderImpl {

            V23BuilderImpl() {
            }

            V23BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                super(policy);
            }

            @Override
            public void detectCleartextNetwork() {
                mBuilder.detectCleartextNetwork();
            }

            @Override
            public void penaltyDeathOnCleartextNetwork() {
                mBuilder.penaltyDeathOnCleartextNetwork();
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        private static class V24BuilderImpl extends V23BuilderImpl {

            V24BuilderImpl() {
            }

            V24BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                super(policy);
            }

            @Override
            public void penaltyDeathOnFileUriExposure() {
                mBuilder.penaltyDeathOnFileUriExposure();
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private static class V26BuilderImpl extends V24BuilderImpl {

            V26BuilderImpl() {
            }

            V26BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                super(policy);
            }

            @Override
            public void detectUntaggedSockets() {
                mBuilder.detectUntaggedSockets();
            }

            @Override
            public void detectContentUriWithoutPermission() {
                mBuilder.detectContentUriWithoutPermission();
            }
        }

        @TargetApi(Build.VERSION_CODES.P)
        private static class V28BuilderImpl extends V26BuilderImpl {

            V28BuilderImpl() {
            }

            V28BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                super(policy);
            }

            @Override
            public void detectNonSdkApiUsage() {
                mBuilder.detectNonSdkApiUsage();
            }

            @Override
            public void permitNonSdkApiUsage() {
                mBuilder.permitNonSdkApiUsage();
            }

            @Override
            public void penaltyListener(
                    @NonNull Executor executor,
                    @NonNull final OnVmViolationListener listener
            ) {
                mBuilder.penaltyListener(
                        executor,
                        new StrictMode.OnVmViolationListener() {

                            @Override
                            public void onVmViolation(Violation violation) {
                                listener.onVmViolation(violation);
                            }
                        }
                );
            }
        }

        @TargetApi(Build.VERSION_CODES.Q)
        private static class V29BuilderImpl extends V26BuilderImpl {

            V29BuilderImpl() {
            }

            V29BuilderImpl(@NonNull StrictMode.VmPolicy policy) {
                super(policy);
            }

            @Override
            public void detectImplicitDirectBoot() {
                mBuilder.detectImplicitDirectBoot();
            }

            @Override
            public void detectCredentialProtectedWhileLocked() {
                mBuilder.detectCredentialProtectedWhileLocked();
            }
        }
    }
}
