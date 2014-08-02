java-plugin-project
===================

Fancies a Plugin Loader

By default plugins are looked into bin/plugins folder. All the class implementing "PrintPlugin" interface are added to the plugins list at run time.

Command line arguments:
------------------------

RegisterDetails.java class accepts two arguments, if provided :
1. output folder path (directory) (without trailing slashes)
2. plugins folder path (directory)


To write a new printing plugin:
-------------------------------

Implement "alienRegister.PrintPlugin" interface in your class
and keep the package as "plugins" in your new plugin.
Add the compiled .class file to bin/plugins folder

If you are using eclipse, just add your plugin class in plugins package and then compile.

For PDF plugin to work, make sure resources/itextpdf-5.5.2.jar is in your path.


For running the project, import all the jars in resources/ folder.
In case of any issues, feel free to mail or call me.

Thanks :)