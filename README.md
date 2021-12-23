# FORCE UPDATE RENESISTECH

Library to check the latest version of app and Force user to update app on latest version


# LIBRARY USAGE

```
	val appForceUpdate = RenesisForceUpdate(ACTIVITY)
        appForceUpdate.startCheckForUpdatedVersion("YOUR APP SLUG","YOUR APP VERSION NAME") {
            when(it){
                ForceUpdateCodes.INTERNET_EXCEPTION->{
                    showMessage("Internet problem")
                }
                ForceUpdateCodes.SERVER_EXCEPTION->{
                    showMessage("Internal server error")
                }
                ForceUpdateCodes.VERSION_MATCHED->{
                    showMessage("Version  successfully matched")
                }
                ForceUpdateCodes.VERSION_NOT_MATCHED->{
                    showMessage("Version not matched")
                }
            }
        }	
```

You can also customize app colors and logo accoridng to your app theme

```

	 val appForceUpdate = RenesisForceUpdate(this)
        appForceUpdate.cardBackgroundColor=R.color.colorBlackWhite
        appForceUpdate.buttonBackgroundColor=R.color.colorBlackWhite
        appForceUpdate.buttonTextColor=R.color.colorBlackWhite
        appForceUpdate.contentColor=R.color.colorBlackWhite
        appForceUpdate.defaultLogo=R.drawable.android
	
```	



# DOWNLOD

Download the latest AAR from Maven Central or grab via Gradle:

Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```  
Step 2. Add the dependency ( App Level )

```
	dependencies {
	        implementation 'com.github.jaidi2858:ForceUpdateRenesisTech:1.0.0'
	}
```
  
  
