Therapy Match
===

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview
### Description
Therapy Match is an app that allows users to match based on similar issues and location.

### App Evaluation
- **Category:** Social Networking
- **Mobile:** This app would be primarily developed for mobile but would be just as viable on a computer, such as tinder, reddit and other similar apps. 
- **Story:** Gives users other users' profiles and if both users "match", they would have access to one another's posts and DMs.
- **Market:** Any individual can use this app, but for safety it is only available to 18+ individuals
- **Habit:** This app could be used as often or unoften as the user wanted depending the user's preferences and condition.
- **Scope:** First, we would start with matching users and posts, then we would add other features such as milestone achievements, public/private profiles(anyone can add a public profile, but a private profile has to "match"), group creating, group and individual calling.

## Product Spec
### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can login/create account
* Will show other individual user profiles based off location/common addictions (it would be anonymous like reddit)
* Users can swipe and match (similar to Tinder)
* Users can post
* Users can see posts of others they've matched with

**Optional Nice-to-have Stories**

* Users can create groups
* Users can send Direct Messages to people they've matched with
* Users can send messages in groups
* Users can change from public to private and vice versa.
* Groups can change from public to private
* Users get milestone achievement trophies.
* Users can connect with group therapy centres *
* Users can call through the app(video and/or audio)
* Users can choose preference for individuals/group therapy centres *
* Group therapy centres can create account (they would need their own signup pages)

### 2. Screen Archetypes

* Launch
* Login 
* Sign up - User signs up or logs into their account
* Other user profiles
   * Upon login/signup, user can view other profiles and swipe left(not interested) or swipe right(interested).
* User Profile Screen 
   * Allows user to upload a photo and fill in information that is interesting to them and others (all users are anonymous)
* Post Screen
   * Allows user to see posts by other users they have matched with.
* Message Screen
   * Shows user a list of all available DMs open to them
* DM Screen
    * Opens the DM of the particular user the current user selected.



## Weekly Stories Milestones

Week 4

* Sign up/Log in to the app
* Logout from app
* User can make a post
* User can view posts by their matches

Week 5

* Users can see other users profiles and match

Week 6


Week 7



## Wireframes
<img src="https://i.imgur.com/lNQ4xRy.png" width=800><br>

### [BONUS] Digital Wireframes & Mockups
<img src="" height=200>

### [BONUS] Interactive Prototype
<img src="" width=200>

### Models
#### Model: User
| Property     | Type      | Description   |
| :---         |    :----: |          ---: |
| userID       | String    |               |
| username     | String    |               |
| password     | String    |               |
| profileImage | ParseFile |               |
| matches      | List<User>|               |
| userIssues   | List<string>|               |

#### Model: Post
| Property   | Type   | Description   |
| :---       | :----: |          ---: |
| description| String |               |
| postImage  | ParseFile|             |
| user       | User   |               |
| createdAt  | Date   |               |

#### Model: Location
| Property    | Type             | Description   |
| :---        |    :----:        |          ---: |
| userLatitude  | String           |               |
| userLongitude        | String  |               |
| user       | User |               |


