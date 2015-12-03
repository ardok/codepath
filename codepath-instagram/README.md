# codepath-instagram

This is a simple Android application for displaying some hard coded Instagram stream

## DAY 1

Time spent: 4 hours spent in total

Completed user stories:

 * [x] Required: User can scroll through current popular posts from Instagram
 * [x] Required: For each post displayed, user can see the following details: (graphic, caption, username, user profile image, relative timestamp, like count)
 * [x] Required: Display each user profile image as a circle
 * [x] Required: Display a nice default placeholder graphic for each image during loading
 * [x] Optional: Make your app look as close to the style and proportions as the mock up provided
 
Notes:

* Not sure about the square picture. But it looks square to me. Just did `fit()` call with Picasso, and on `ImageView` I added `adjustViewBounds=true` as well as `fitXY`.

Walkthrough of all user stories:

![Video Walkthrough](http://i.imgur.com/RPBV6ui.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).


## DAY 2

Time spent: 6 hours spent in total

Completed user stories:

 * [x] Required: Connect the app with the Instagram API and get real time data using the android-async-http library library.
 * [x] Required: Show the last 2 comments for each photo
 * [x] Required: User can view all comments for an image within a separate activity.
 * [x] Required: User can share an image to their friends or email it to themselves.
 * [x] Optional: Use the Butterknife library to remove all findViewById(...) calls.
 * [x] Optional: Robust error handling, check if internet is available, handle error cases, network failures.

Notes:

* Added swipe to refresh.
* I have trouble with getting context in adapter. I'm not sure what the best way is. One of the TAs mentioned that the best way would be to pass in a callback into the adapter, which is gonna be really cumbersome. Is there no easier way?

Walkthrough of all user stories:

![Video Walkthrough](http://i.imgur.com/XaJgqXm.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## DAY 3

Time spent: 9 hours spent in total

Completed user stories:

 * [x] Required: User can login to Instagram using OAuth login.
 * [x] Required: User can view their own feed.
 * [x] Required: User can search for a user by username.
 * [x] Required: User can search for a tag.
 * [x] Optional: Include a ProgressBar during network loading.
 * [x] Optional: User can get a grid of photos by clicking on a search result (i.e. a user or tag).

Notes:

* I have no idea how to manage data and all that. Like for example, we have SearchFragment that has 2 fragments inside it. How do you talk in between fragments?
  I want to set up the click listener on the search result adapter that will start the PhotoGridActivity.
  The problem is that the recommended way to set the click listener is to set it in the constructor of view holder.
  There's no way for me to get the current item at the click event.
  So, Nick (that's his name, right?) said that the easiest way would be to use setTag and getTag on the View (container). Hence, that's what I did.
* Managing fragment backstack.
  I don't know exactly how to manage this because we're using some pager adapter now. Maybe just need to read more about it.
* Back button on main activity.
  Can't exit the app. Keeps going to login activity and login activity will redirect back to main activity.
* Back button on action bar (set home up enabled)
  Why the Android doc says to use NavUtil? It looks like using super.onBackPressed is sufficient?
* I couldn't find a way to add a Toolbar on login activity


Walkthrough of all user stories:

Sorry, no walktrough. There's no way to block my Instagram password from being typed.
Opening Browser settings in both built-in emulator and genymotion crashes the app.
Tried installing Chrome in Genymotion and I couldn't even open it. Keeps crashing.
