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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Closeable;

public class StrictModeCompat {

    private static final StrictModeImpl STRICT_MODE_IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            STRICT_MODE_IMPL = new StrictModeImplV11();
        } else {
            STRICT_MODE_IMPL = new StrictModeImplBase();
        }
    }

    private StrictModeCompat() {
    }

    /**
     * A convenience wrapper that takes the current
     * {@link ThreadPolicy} from {@link #getThreadPolicy}, modifies it
     * to permit disk reads, and sets the new policy
     * with {@link #setThreadPolicy}, returning the old policy so you
     * can restore it at the end of a block.
     *
     * @return the old policy, to be passed to setThreadPolicy to
     * restore the policy.
     */
    @NonNull
    public static StrictMode.ThreadPolicy allowThreadDiskReads() {
        return STRICT_MODE_IMPL.allowThreadDiskReads();
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
        return STRICT_MODE_IMPL.allowThreadDiskWrites();
    }

    /**
     * Enable the recommended StrictMode defaults, with violations just being logged.
     * <p/>
     * <p/>This catches disk and network access on the main thread, as
     * well as leaked SQLite cursors and unclosed resources.  This is
     * simply a wrapper around {@link #setVmPolicy} and {@link
     * #setThreadPolicy}.
     */
    public static void enableDefaults() {
        STRICT_MODE_IMPL.enableDefaults();
    }

    /**
     * Returns the current thread's policy.
     *
     * @see StrictMode#getThreadPolicy()
     */
    @Nullable
    public static StrictMode.ThreadPolicy getThreadPolicy() {
        return STRICT_MODE_IMPL.getThreadPolicy();
    }

    /**
     * Gets the current VM policy.
     */
    @Nullable
    public static StrictMode.VmPolicy getVmPolicy() {
        return STRICT_MODE_IMPL.getVmPolicy();
    }

    /**
     * For code to note that it's slow.  This is a no-op unless the
     * current thread's {@link android.os.StrictMode.ThreadPolicy} has
     * {@link android.os.StrictMode.ThreadPolicy.Builder#detectCustomSlowCalls}
     * enabled.
     *
     * @param name a short string for the exception stack trace that's
     *             built if when this fires.
     */
    public static void noteSlowCall(@NonNull String name) {
        STRICT_MODE_IMPL.noteSlowCall(name);
    }

    /**
     * Sets the policy for what actions on the current thread should
     * be detected, as well as the penalty if such actions occur.
     * <p/>
     * <p/>Internally this sets a thread-local variable which is
     * propagated across cross-process IPC calls, meaning you can
     * catch violations when a system service or another process
     * accesses the disk or network on your behalf.
     *
     * @param policy the policy to put into place
     */
    public static void setThreadPolicy(StrictMode.ThreadPolicy policy) {
        STRICT_MODE_IMPL.setThreadPolicy(policy);
    }

    /**
     * Sets the policy for what actions in the VM process (on any
     * thread) should be detected, as well as the penalty if such
     * actions occur.
     *
     * @param policy the policy to put into place
     */
    public static void setVmPolicy(StrictMode.VmPolicy policy) {
        STRICT_MODE_IMPL.setVmPolicy(policy);
    }

    /**
     * Set Thread and VM policies in one method.
     *
     * @param threadPolicy the thread policy to put into place
     * @param vmPolicy     the vm policy to put into place
     */
    public static void setPolicies(StrictMode.ThreadPolicy threadPolicy,
                                   StrictMode.VmPolicy vmPolicy) {
        setThreadPolicy(threadPolicy);
        setVmPolicy(vmPolicy);
    }

    private interface StrictModeImpl {

        StrictMode.ThreadPolicy allowThreadDiskReads();

        StrictMode.ThreadPolicy allowThreadDiskWrites();

        void enableDefaults();

        @Nullable
        StrictMode.ThreadPolicy getThreadPolicy();

        @Nullable
        StrictMode.VmPolicy getVmPolicy();

        void noteSlowCall(@NonNull String name);

        void setThreadPolicy(StrictMode.ThreadPolicy policy);

        void setVmPolicy(android.os.StrictMode.VmPolicy policy);
    }

    private static class StrictModeImplBase implements StrictModeImpl {

        @Override
        public StrictMode.ThreadPolicy allowThreadDiskReads() {
            return StrictMode.allowThreadDiskReads();
        }

        @Override
        public StrictMode.ThreadPolicy allowThreadDiskWrites() {
            return StrictMode.allowThreadDiskWrites();
        }

        @Override
        public void enableDefaults() {
            StrictMode.enableDefaults();
        }

        @Override
        public StrictMode.ThreadPolicy getThreadPolicy() {
            return StrictMode.getThreadPolicy();
        }

        @Override
        public StrictMode.VmPolicy getVmPolicy() {
            return StrictMode.getVmPolicy();
        }

        @Override
        public void noteSlowCall(@NonNull String name) {
        }

        @Override
        public void setThreadPolicy(StrictMode.ThreadPolicy policy) {
            StrictMode.setThreadPolicy(policy);
        }

        @Override
        public void setVmPolicy(StrictMode.VmPolicy policy) {
            StrictMode.setVmPolicy(policy);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static class StrictModeImplV11 extends StrictModeImplBase {

        @Override
        public void noteSlowCall(@NonNull String name) {
            StrictMode.noteSlowCall(name);
        }
    }

    public static class ThreadPolicy {

        private ThreadPolicy() {
        }

        public final static class Builder {

            private final ThreadPolicy.BuilderImpl mBuilder;

            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBuilder = new V23BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mBuilder = new V11BuilderImpl();

                } else {
                    mBuilder = new BaseBuilderImpl();
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
             */
            public Builder detectUnbufferedIo() {
                mBuilder.detectUnbufferedIo();
                return this;
            }
        }

        private interface BuilderImpl {

            StrictMode.ThreadPolicy build();

            void detectAll();

            void detectCustomSlowCalls();

            void detectDiskReads();

            void detectDiskWrites();

            void detectNetwork();

            void detectResourceMismatches();

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

            void permitResourceMismatches();

            void detectUnbufferedIo();
        }

        private static class BaseBuilderImpl implements BuilderImpl {

            final StrictMode.ThreadPolicy.Builder builder = new StrictMode.ThreadPolicy.Builder();

            @Override
            public StrictMode.ThreadPolicy build() {
                return builder.build();
            }

            @Override
            public void detectAll() {
                builder.detectAll();
            }

            @Override
            public void detectCustomSlowCalls() {
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
            public void detectResourceMismatches() {
            }

            @Override
            public void penaltyDeath() {
                builder.penaltyDeath();
            }

            @Override
            public void penaltyDeathOnNetwork() {
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
            public void penaltyFlashScreen() {
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
            public void permitCustomSlowCalls() {
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

            @Override
            public void permitResourceMismatches() {
            }

            @Override
            public void detectUnbufferedIo() {
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private static class V11BuilderImpl extends BaseBuilderImpl {

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
        }

        @TargetApi(Build.VERSION_CODES.M)
        private static class V23BuilderImpl extends V11BuilderImpl {

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
        private static class V26BuilderImpl extends V11BuilderImpl {

            @Override
            public void detectUnbufferedIo() {
                builder.detectUnbufferedIo();
            }
        }
    }

    public static final class VmPolicy {

        private VmPolicy() {
        }

        public static final class Builder {

            @NonNull
            private final BuilderImpl mBuilder;

            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mBuilder = new V24BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBuilder = new V23BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    mBuilder = new V18BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBuilder = new V16BuilderImpl();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mBuilder = new V11BuilderImpl();

                } else {
                    mBuilder = new BaseBuilderImpl();
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
             * @see android.support.v4.content.FileProvider
             * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
             */
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
             * {@link Intent#FLAG_GRANT_WRITE_URI_PERMISSION}.<p/>
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
             * <p/>This currently does not detect sockets created in native code.
             */
            public Builder detectUntaggedSockets() {
                mBuilder.detectUntaggedSockets();
                return this;
            }
        }

        private interface BuilderImpl {

            StrictMode.VmPolicy build();

            void detectActivityLeaks();

            void detectAll();

            void detectCleartextNetwork();

            void detectFileUriExposure();

            void detectLeakedClosableObjects();

            void detectLeakedRegistrationObjects();

            void detectLeakedSqlLiteObjects();

            void penaltyDeath();

            void penaltyDeathOnCleartextNetwork();

            void penaltyDeathOnFileUriExposure();

            void penaltyDropBox();

            void penaltyLog();

            void setClassInstanceLimit(@NonNull Class<?> klass,
                                       @IntRange(from = 0) int instanceLimit);

            void detectContentUriWithoutPermission();

            void detectUntaggedSockets();
        }

        private static class BaseBuilderImpl implements BuilderImpl {

            final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

            @Override
            public StrictMode.VmPolicy build() {
                return builder.build();
            }

            @Override
            public void detectActivityLeaks() {
            }

            @Override
            public void detectAll() {
                builder.detectAll();
            }

            @Override
            public void detectCleartextNetwork() {
            }

            @Override
            public void detectFileUriExposure() {
            }

            @Override
            public void detectLeakedClosableObjects() {
            }

            @Override
            public void detectLeakedRegistrationObjects() {
            }

            @Override
            public void detectLeakedSqlLiteObjects() {
                builder.detectLeakedSqlLiteObjects();
            }

            @Override
            public void penaltyDeath() {
                builder.penaltyDeath();
            }

            @Override
            public void penaltyDeathOnCleartextNetwork() {
            }

            @Override
            public void penaltyDeathOnFileUriExposure() {
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
            public void setClassInstanceLimit(@NonNull Class<?> klass,
                                              @IntRange(from = 0) int instanceLimit) {
            }

            @Override
            public void detectContentUriWithoutPermission() {
            }

            @Override
            public void detectUntaggedSockets() {
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private static class V11BuilderImpl extends BaseBuilderImpl {

            @Override
            public void detectActivityLeaks() {
                builder.detectActivityLeaks();
            }

            @Override
            public void detectLeakedClosableObjects() {
                builder.detectLeakedClosableObjects();
            }

            @Override
            public void setClassInstanceLimit(@NonNull Class<?> klass,
                                              @IntRange(from = 0) int instanceLimit) {
                builder.setClassInstanceLimit(klass, instanceLimit);
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private static class V16BuilderImpl extends V11BuilderImpl {

            @Override
            public void detectLeakedRegistrationObjects() {
                builder.detectLeakedRegistrationObjects();
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        private static class V18BuilderImpl extends V16BuilderImpl {

            @Override
            public void detectFileUriExposure() {
                builder.detectFileUriExposure();
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        private static class V23BuilderImpl extends V18BuilderImpl {

            @Override
            public void detectCleartextNetwork() {
                builder.detectCleartextNetwork();
            }

            @Override
            public void penaltyDeathOnCleartextNetwork() {
                builder.penaltyDeathOnCleartextNetwork();
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        private static class V24BuilderImpl extends V23BuilderImpl {

            @Override
            public void penaltyDeathOnFileUriExposure() {
                builder.penaltyDeathOnFileUriExposure();
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private static class V26BuilderImpl extends V23BuilderImpl {

            @Override
            public void detectUntaggedSockets() {
                builder.detectUntaggedSockets();
            }

            @Override
            public void detectContentUriWithoutPermission() {
                builder.detectContentUriWithoutPermission();
            }
        }
    }
}
