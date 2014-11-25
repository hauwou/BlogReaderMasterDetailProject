BlogReaderMasterDetailProject
=============================

Creating a blog post reader. 

Build this app with the intention of demonstrating the creation of the Master/detail app style from scratch.

The app started out with using dummy data, then eventually switched to accessing JSON data from the web.

Along the way, created/verified http connection and sanitize data, manipulated JSON data, output to ListView using adapter

Created dialog popup and progress spinner UI.

Getting the ListView's empty text view is not the same as getting a normal Text (not by findViewById)

Using ArrayList instead of Array for flexibility.  

ArrayAdapter can hold simple data types (int, string, bool...). SimpleAdapter can hold more complex data.

Use SimpleAdapter is adapt ArrayList of complex data types, Use ArrayAdapter to adapt arrays of simple data types.

create new activity and intents. Set/Get intent data

Use Intents (explicit and implicit) to view web url upon clicking on a listview item.

Create share intents and createchooser dialog to allow users to pick app to share
