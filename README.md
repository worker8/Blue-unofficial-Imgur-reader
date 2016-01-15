## Imgur-Discovery
This is an Android application that lets you browse and discover images from [imgur.com](http://imgur.com). While this does not serve as a complete client for imgur.com, it provides a clean and intuitive UI for browsing.

#### Features
* Browse imgur.com
* Switch to different imgur section
* Popular list of imgur section
* Floating Action Button for picking a random imgur section (fun!)
* Support gif, gifv, webm, mp4 rendering
* Support imgur album in swiping mode
* Image deep zooming mode
* Loading low but good enough image resolution to speed up & save data
* Sharing image link
* Download image
* Open image in browser
* Copy image link

## Screenshots
![](http://i.imgur.com/hMZWvYlm.png) ![](http://i.imgur.com/wvyLURtm.png) ![](http://i.imgur.com/C0OdYYsm.png)

[**Click here** for more](http://imgur.com/a/nVI0C)

## Project Setup
1. Make sure you are running jdk8 because this project uses retrolambda that depends on java 8 features, refer to this:
https://github.com/evant/gradle-retrolambda
2. `git clone` this project
3. Open the project and let gradle sync, it should run just fine. otherwise, file an issue

## Code explanation
#### Main Flow
At this moment, this app does not include the rendering of comments and the login feature. So it is still relatively simply. The entire flow of the app revolves around `MainActivity.java`. So the easiest way to understand the codebase is to start with `MainActivity.java`!

When you open up the app, you will see a scrollable list of images card, this is `MainActivity.java`. The ListView adapter used here is `ImageAdapter.java`.

There is a navigation drawer that you can open up by sliding from the left, or tapping on the burger menu button. The adapter code used in the navigation drawer can be found in `NavAdapter.java`.

When you click on any images, it will open up a detail view. In this detail view, you can view each posts in a more detail manner.

#### Dispatching work to other activities
Imgur posts come in various different formats, it can mainly be broken down into 3 categories:
 * normal image (.png .jpeg)
 * animated image (.gif .gifv .webm .mp4)
 * album (it contains a link to multiple images)
 
When the image is clicked, `ImgurLinkDispatcher.java` will determine which type of image it is, and launches the appropriate actvities as shown below:

* normal image is handled by `ImageViewerActivity.java`
* animated image is handled by `GifActivity.java`
* album is handled by `ImgurAlbumActivity.java`

#### Structure
* `app/` folder contains most of the source code
* `jimgur/` folder contains source code of the wrapper to Imgur API (https://api.imgur.com/)

## Known issues
* HD mode sometimes don't work on very long images
* Long images become blurry (because the ImageView fit by scaling to width)
* Long title get cut off in image cardview (need to show the full title in ImageViewerActivity and GifActivity
* Crash is reported for clicking on HD mode (need more info on this)
* Crash might happen if you tap on HD button before the non-HD image is loaded


## Future improvements
* Add tests
* Support tablet
* Test with more devices
* Add swipe mode, so users can navigate to the nest post easily
* Show comments
* Add login
* Add search feature for subreddit
* Etc...

## Targeted API version and devices
API:

    minSdkVersion = 14 (this is 96% of the devices at the when this doc is written)
  
Supported screen sizes:

    only mobile phone is supported, tablet might run this app fine, but it's not tested.


## License
    TODO: Add one...
