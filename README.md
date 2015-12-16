# Assignment 4 - Add Implicit Intents and HTTP data loading to Beverage app.

## Author
Zachary Dwyer


## Description

This assignment will require you to either update my version of assignment 3, or your own version. This repository comes with my finished version of assignment 3. If you would rather use your version, you must replace all of the files in this project with your files. This way you can still make a pull request to submit the assignment.

Once a user is on the detail page for a beverage item, there should be two additional buttons added that can be pressed. A Select Contact Button, and a Send Email button. The Select Contact button will allow a user to select a contact from the contacts application. The Send Email button will allow the user to send an email to the contact that was selected.

If the device has a default Contacts App, then the Select Contact button should be enabled, and the Send Email button disabled until a contact is selected. If the application does not have a default Contacts App, the application should disable the Select Contacts button and enable the Send Email button. If it is the case where a contacts app does not exist, the user should still be able to send an email, however there will be no way to pre-populate the 'to' field nor personalize it.

When clicking the Select Contact button, the app should launch the default contacts app and allow the user to select a contact to send a email to.
When clicking the Send Email button, the app should take the email address of the selected user, and use that as the recipient for the email. The email should have a Subject auto-generated based on a pre-defined message. The body of the email should address the contact that was selected by using the contacts name. This will help personalize the message. Lastly, the email body should also contain all of the information about the specific item.

In addition to adding the Implicit Intents to select a contact and send an email, the app must now load it's data from a remote web service vs. loading it from the CSV file.

The exact same data can be pulled by making a HTTP request to the following URL. If you would like to see what gets returned, you can can simply type the following URL into a web brower and see what gets returned.

http://barnesbrothers.homeserver.com/beverageapi

The data comes back as JSON data that must be parsed into the collection of Beverages. Some of the work that was being done in the BeverageCollection (BeverageLab depending on what you named it), may need to move to the fragment that hosts the RecyclerView. That way onPostExecute can easily finish wiring up the RecyclerView after the data gets returned.

In order to get the returned JSON string from the web service into something that can begin to be parsed, the following code must be used to turn the JSONString into an object. The string returns an Array. It does NOT return a Object like the JSON from the book does. Therefore the inital parse from string to something must be to a JSONArray and not a JSONObject.

    JSONArray jsonBeverages = new JSONArray(jsonString);

Here are some screen shots showing what the new details page and email should look like.

![Application Detail Portrait Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment4a.jpg "Application Detail Portrait Layout")
![Application Email Message Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment4b.jpg "Application Email Message Layout")

Solution Requirements:

* Add button for Selecting a Contact
* Add button for Sending and Email
* Proper disabled / enable of buttons based on above description
* Pull email address from contact
* Auto-generate the email to, subject, and body
* Load Beverage data from HTTP web service

Optional Extra Credit:

* Use SQLite database in place of CSV / Web Service
* Pre-load SQLite database with data from Web Service

### Notes

You will need to add a permission to use contacts to the Android Manifest file. The book only fetches the users name, and therefore does not need to ask permission. We would like to get the email for the user, so permission is needed. The permission is called:

    <uses-permission android:name="android.permission.READ_CONTACTS" />

The book does not cover how to pull the email address of a contact. It is a little more work than just the name. It requires making two queries to the contacts database. This stackoverflow answer should help you figure out what you need to do to get the email address for a contact. Because we are requesting data from a contacts 'database', a lot of the syntax is similar to what is done when interacting with SQLite.

http://stackoverflow.com/questions/10117049/get-only-email-address-from-contact-list-android

You will also need to add permission to the manifest to allow access to the Internet. The book DOES cover this.

You will probably need to add an email account and a contact to your virtual device in order to test this functionality.

### Extra Credit

For an extra 20 points of assignment extra credit you can do the following:

Setup a SQLite database to maintain the information for the Beverages. When the database is initally setup, the database should pre-populate it's data by accessing the HTTP web URL mentioned above. There are 2 changes here. The collection of data used to 'run' the application will pull from the database and NOT the web service. The database will initally pull from the web service when it is being setup for the first time, and then never use the web service again. (Unless the app is uninstalled and reinstalled).

The application must do all of the above work to get the extra credit. In order to satisfy the web service grading part of the assignment, the application must either load the Beverage data from the web service when the application starts up (no database), or load the Beverage data when the database is created (with database). If there is no web service functionality for one of those two features, then you can not get any points for that section of grading.

## Outside Resources Used
* Issue with To field not populating - http://stackoverflow.com/questions/9097080/intent-extra-email-not-populating-the-to-field
* Using .setType("message/rfc822") - http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
* General intents - http://developer.android.com/guide/components/intents-filters.html
* CommonDataKinds - http://developer.android.com/reference/android/provider/ContactsContract.CommonDataKinds.Email.html
* Decimal formatter - http://stackoverflow.com/questions/8819842/best-way-to-format-a-double-value-to-2-decimal-places
* Internet permission - http://stackoverflow.com/questions/2378607/what-permission-do-i-need-to-access-internet-from-an-android-application
* System.lineSeparator() not working for making newline escape sequences - http://stackoverflow.com/questions/19150092/system-lineseparator-doesnt-exist-in-android

* Getting the name and email largely relies on the code mentioned in http://stackoverflow.com/questions/10117049/get-only-email-address-from-contact-list-android (I had a lot of issues trying to figure out how to access a contact's email)

## Known Problems, Issues, And/Or Errors in the Program
None known. 