# HackerNews
An Android app for getting HackerNews top stories feed

There are three screens on this app:

1. Top Stories 
2. Comments
3. Replies

Below is an introduction to the functionality and screen flow of the app.

# 1. Top Stories

**a) Top stories would be loaded upon the first launch of the app.**

![ScreenShot](https://s33.postimg.org/xw6sb1aof/Screenshot_1511836757.png "Top Stories")

**b) A browser selection will be shown when user click on the news item.**

![ScreenShot](https://s33.postimg.org/5xcmk13v3/Screenshot_1511836763.png "Browser Selection")

**c) The selected browser will launch the news when choices are made.**

![ScreenShot](https://s33.postimg.org/d0khzsehb/Screenshot_1511836794.png "Browser Loads News")

# 2. Comments

**a) Comments will be loaded when user click on the "{Number of comments} comments" on the story item.**

![ScreenShot](https://s33.postimg.org/aj8qslf67/Screenshot_1511836802.png "Comments")

# 3. Replies

**a) Replies will be loaded when user click on the "View replies" on the comment item.**

![ScreenShot](https://s33.postimg.org/hmgm88fgv/Screenshot_1511836806.png "Replies")

# Others

1. On all three screens, only 10 items are loaded each time. 

2. "Load more" will be shown if more than 10 items are available.

3. Currently there is no pagination implemented, so if "Load more" was clicked, there is no way of showing previous items. Clicking on the back button will only close the current screen and go back to the previous screen.

4. To generate code coverage report, run the following command in the terminal at project level directory: <br>"gradlew clean jacocoTestReport --stacktrace"

5. The generated report can be found at: <br>{Project Directory}/app/build/reports/jacoco/jacocoTestReport/html/index.html


Test 1
