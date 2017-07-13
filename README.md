[![Release](https://jitpack.io/v/trinhlbk1991/Notifierce.svg)](https://jitpack.io/#trinhlbk1991/Notifierce)

# Notifierce
A fierce notification library for Android

#Download

1. Add the JitPack repository to your build file

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add the dependency

```
	dependencies {
	        compile 'com.github.trinhlbk1991:Notifierce:1.0.0'
	}
```

#Usage

Custom:

```
Notifierce.builder(this)
        .setTitle("Notifierce")
        .setTitleColor(Color.BLUE)
        .setMessage("Welcome to Iced Tea Labs")
        .setMessageColor(Color.WHITE)
        .setBackgroundColor(Color.GRAY)
        .setIcon(R.mipmap.ic_launcher)
        .autoHide(false)
        .build()
        .show();
```

Error:

```
Notifierce.builder(this)
        .setTitle("Error")
        .setMessage("This is the error message")
        .setTypeface(Typeface.createFromAsset(this.getAssets(), "font/" + "RobotoCondensed-Regular.ttf"))
        .build()
        .error();
```

Success:

```
Notifierce.builder(this)
        .setTitle("Success")
        .setMessage("This is the success message")
        .build()
        .success();
```

Warning:

```
Notifierce.builder(this)
        .setTitle("Warning")
        .setMessage("This is the warning message")
        .build()
        .warning();
```
