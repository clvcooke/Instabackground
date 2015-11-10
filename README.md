InstaBackground
===============
Your favourite food pictures are now your background!

####Getting started
**Install Android Studio**
- The most recent version can be found here: http://developer.android.com/sdk/index.html
- If you have a previous version installed then you can either update or uninstall/reinstall

**Install SourceTree/GitHub**
- These are two good Git clients that I know of, feel free to use a different one or just use the command line, but I probably will be less help if you do so
- SourceTree is a bit more complex than GitHub but it gives you a lot more control over what's happening and a better overview of the history and branches
- GitHub is pretty simplified but its sometimes hard to know what's going on (IMO)

**Read**

Android is a very broad topic, and on top of that some of you will be using Git for the first time. While there is a whole lot of stuff to learn here are some sites that would be a good start.
- Basics of Android: http://developer.android.com/about/start.html
- Basics of Git (first three chapters): http://git-scm.com/book/en/v2
- Java: http://heather.cs.ucdavis.edu/~matloff/Java/JavaIntro.html

###Notes On Development
Just some general notes on development, and how I think we should be doing it

**Don't just do stuff**

As a developer you should (hopefully) be able to explain all your decisions, as they should stem from logic. There will be times when you don't know why something works, but thats fine as long as you explain that it is the case along with what else you tried. The worst case scenario is for there to be code that no one can figure out, yet we can't replace because we don't know what its doing.

**Naming**

Naming is very very very important. There are certain conventions which we **ALL** must follow in order to develop productively. They will be outlined at the bottom of this document.

**Issues**

I like working using an issue driven system, which means whenever you are developing you are working towards a specific issue. It might seem like there is a lot of them but that's OK. Whenever you have nothing to do either find and issue that has been already been raised or raise one yourself and get to work. Issues allow for a track-able, and accountable discussion on development. If you ever start development on something which there is no issue for stop. If everything has an issue then we always know who is doing what and we don't have people duplicating each others work. Issue tracking will be handled entirely on Github.

#Naming Conventions

The conventions to follow are pretty simple, name things what they are.

For example an object which asynchronously gathers photos from Instagram could be named: *AsyncPhotoRetriever* (for its type) and *asyncPhotoRetriever* as an instance of that type.

- CONSTANTS_ARE_NAMED_LIKE_THIS
- ClassesAreNamedLikeThis
- functionsAreNamedLikeThis
- variablesAreNamedLikeThis
