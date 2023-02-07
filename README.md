
# Journal App

Project Name:  `Journal App`  
Name:  `Hitarth Kothari`
ID:  `2019A7PS0178G`  
Email:  `f20190178@goa.bits-pilani.ac.in`

## Description

This is a simple Journal app.

## Known Bugs

- An entry can be shared even without saving it
- An entry can have either of its 4 values - title, date, start time or end time as unset.

## Approach

Used safe args for navigation between fragments along with a navcontroller and navhost in main activity. There were two important fragments that were used - `EntryListFragment` and `EntryDetailsFragment` . The main activity served as a container for both the fragments. `EntryListFragment` was responsible for displaying the list of journal items as a `RecyclerView`. From here, navigation to the `EntryDetailsFragment` was possible either by creating a new entry (add entry button) or clicking on any entry and choosing to update it. The `EntryDetailsFragment` updated or added an entry that had 4 values - title, date, start time and end time. This entry could be deleted, shared or saved after updating it.

## Difficulty and time required for assignment

Difficulty : 9.99/10 (Saving 10/10 for the final one)
Time required : So much, that I lost track of it and sleep too :)
