# Custom Screen Saver Application 
How To Apply
- File To Apply : frameworks/base/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
- Position

      private static final String DREAMS_DEFAULT_COMPONENT =
          "dw.koo.android.screensaverservice";
      private static final String SCREENSAVER_PROP = "sys.screensaver.enable";

      Or
      loadStringSetting(stmt, Settings.Secure.SCREENSAVER_COMPONENTS,
                                          com.android.internal.R.string.config_dreamsDefaultComponent);
      loadStringSetting(stmt, Settings.Secure.SCREENSAVER_DEFAULT_COMPONENT,
                                          com.android.internal.R.string.config_dreamsDefaultComponent);
