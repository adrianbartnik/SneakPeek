# SneakPeak App 

Show the movie predictions as well as other information for the SneakPreview at the Cineplex Potsdamer Platz, Berlin.

## Todo
* Recreate statistics upon first start - fix crash with diagramm
  * https://stackoverflow.com/questions/14223270/fragment-member-variables-getting-null-when-accessed-in-onpageselected
  * https://stackoverflow.com/questions/9727173/support-fragmentpageradapter-holds-reference-to-old-fragments
  * https://stackoverflow.com/questions/11631408/android-fragment-getactivity-sometime-returns-null

## New Future
* Differentiate between distributor and studio
* Adding animations
* Export Icon - Script
* Add Changelog to About Dialog
* Fix overlay labels in statistics chart

## Theme
* https://material.io/color/#!/?view.left=0&view.right=1&primary.color=B0BEC5&secondary.color=aed681

## Table design

The table design of **SneakPeek**.

### Movies

* ID
* Title

### Studios

* ID
* Name

### Movie Predictions

* Week
* Movies.ID
* Position

### Studio Predictions

* Week // Currently not in Armins Data
* Movies.ID
* Studios.ID

### Past Movies

* Week
* Movies.ID
