# Assignment 3 - Master Detail Wine List with Fragments

## Author

David Barnes

## Description

Create a master detail application to view the contents of a beverage list.
The master part will display a list of all of the beverage items in the list.
The Detail part will display the details of a specific beverage item.
The User should be able to go up and down items in the list by swiping left or right on a detail page (View Pager).
The App should look like the screen shots I have provided.
There is no need to save any information changed in the application to the CSV file.

The properties of the CSV file are as follows:
1. Item Number
2. Item Description
3. Item Pack Size
4. Item Case Price
5. Whether the item is currently active. This is represented by a string that either says True, or False.

Solution Requirements:

* Master layout (You can use the same layout for both Portrait and Landscape unless you feel the need to make one for each)
* Detail layout (You can use the same layout for both Portrait and Landscape unless you feel the need to make one for each)
* Fragments for all of the layouts (Expect ViewPager. That one is a little different.)
* Ability to click on a list item and start a new Activity that displays the details of the list item.
* Updating information in the detail view should be reflected in the list view when returning to the list.
* Class to represent a Beverage Item (POJO)
* ArrayList for the list of Beverage Items
* RecyclerView to display the master view
* Read in CSV file and create ArrayList of Beverage Items from the data in the CSV file
* Use View Pager to swipe left and right on detail page to go up and down in the list
* Should look like the screen shots provided
* Remember Documentation, and README

![Application Master Portrait Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment3a.jpg "Application Master Portrait Layout")
![Application Detail Portrait Layout](http://barnesbrothers.homeserver.com/cis298/assignmentImages/assignment3b.jpg "Application Detail Portrait Layout")

### Notes

The book does not cover how to read in a CSV file. We will do something in class to demonstrate how to get the CSV read in.
The location of the file to be read in is in the following paths:
Inside Android Studio: app/res/raw/
Through File System:   app/src/main/res/raw/

## Outside Resources Used

Just the Big Nerd Ranch Book

## Known Problems, Issues, And/Or Errors in the Program

None that I know of.
