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

