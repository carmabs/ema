# EMA

**EMA** is a library based on MVI architecture, with concurrency based on Kotlin coroutines, dependency injection with **KOIN**, and view interfaces with fragments/compose support.

This way you can create by a very easy and fast way robust and maintenable apps due to **EMA** support, it handles lots of classes that helps the developer to develop new features respecting the MVI architecture.


# USE IT

To use the library add the following dependencies to gradle:

Add the maven repository:
  
      allprojects {
		    repositories {
			    ...
			    maven { url 'https://jitpack.io' }
    	  }
      }

Add the **EMA** dependencies, it include ema-core library and its library dependencies.

    dependencies {
          implementation 'com.github.carlosmateo89.Ema:ema-android:2.3.0'
    }

For compose support you can add the following.

    dependencies {
          implementation 'com.github.carlosmateo89.Ema:ema-android:2.3.0'
    }

If the module is pure kotlin, add the ema-core library
  

    dependencies {
	      implementation 'com.github.carlosmateo89.Ema:ema-core:2.3.0'
    }




