InstaBackground
===============
Your favourite food pictures are now your background


#About
The basic concept of InstaBackground is to allow users to set their phone or watch background to any instagram feed. Be it a specific user, a group of users or their own feed. The app will cache a set amount of pictures on the phone per day and cycle them throughout the day. The same goes for the watchface.

Which photos show up, and how often they are cycled can be customized by the user but there will be a nice default that most will probably stick with.

##Phone
Since in order for a smartwatch to be of any use there must be an android phone also the app will be needed, even if you only want to use the watchfaces. The phone app will be where all the customization occurs and where the user can setup what feed they want. There will be some sort of sync on a regular  basis to update the photos on the phone and watch. If no new photos are available no sync will be performed. As of now this will probably occur via parsing of a RSS feed generated from instagram.

##Watch

The watchface will be pretty minimal, simply a photo background with either a digital or analog display ontop for the time. The photos will be cached on device at their ideal resolution. Any heavy processing should be done on the phone or at least only once a day, as battery life is a major issue.


###Customization

Pretty simply, everything should be customizable. From which photos show up to how the photos are displayed. Although it is pretty important to build things with this in mind we will mostly ignore customization untill near the end as to get something that is testable as soon as possible.


#Newbie Guide
As not many people on the project have (any) experience, especially with mobile here is a bunch of stuff for all you newbies.

####Getting started
**Install Android Studio**
- The most recent version can be found here: http://developer.android.com/sdk/index.html
- If you have a previous version installed then you can either update or uninstall/reinstall
-

**Install SourceTree/GitHub**
- These are two good Git clients that I know of, feel free to use a different one or just use the command line, but I probably will be less help if you do so
- SourceTree is a bit more complex than GitHub but it gives you a lot more control over whats happening and a better overview of the histroy and branches
- GitHub is pretty simplified but its sometimes hard to know whats going on (IMO)

**Read**

Android is a very broad topic, and on top of that some of you will be using Git for the first time. While there is a whole lot of stuff to learn here are some sites that would be a good start.
- Basics of Android: http://developer.android.com/about/start.html
- Basics of Git (first three chapters): http://git-scm.com/book/en/v2
- Watchfaces: https://developer.android.com/training/wearables/watch-faces/index.html
- Android Wear: https://developer.android.com/wear/index.html
- Java: http://heather.cs.ucdavis.edu/~matloff/Java/JavaIntro.html

###Notes On Development
Just some general notes on development, and how I think we should be doing it

**Don't just do stuff**

As a developer you should (hopefully) be able to explain all your decisions, as they should stem from logic. There will be times when you don't know why something works, but thats fine as long as you explain that it is the case along with what else you tried. The worst case scenerio is for there to be code that no one can figure out, yet we can't replace because we don't know what its doing.

**Naming**

Naming is very very very important. There are certain conventions which we **ALL** must follow in order to develop productivly. They will be outlined at the bottom of this document.

**Commenting**

This might sound weird, but if your doing things right you shouldn't need to write very many comments. If you code reads well and makes logical sense there is no need to comment it. If it doesn't you may need to rewrite some of it. Comments are a last resort for when your code is too complex, or don't have the time/energy to fix it.

**Issues**

I like working using an issue driven system, which means whenever you are developing you are working towards a specific issue. It might seem like there is a lot of them but thats OK. Whenever you have nothing to do either find and issue that has been already been raised or raise one yourself and get to work. Issues allow for a trackable, and accountable discussion on development. If you ever start development on something which there is no issue for stop. If everything has an issue then we always know who is doing what and we don't have people duplicating each others work. Issue tracking will be handled entirely on Github.




#Naming Conventions

The conventions to follow are pretty simple, name things what they are.

For example an object which asynchronously gathers photos from instagram could be named: *AsyncPhotoRetriever* (for its type) and *asyncPhotoRetriever* as an instance of that type.


- CONSTANTS_ARE_NAMED_LIKE_THIS
- ClassesAreNamedLikeThis
- functionsAreNamedLikeThis
- variablesAreNamedLikeThis
