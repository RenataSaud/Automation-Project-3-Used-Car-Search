# Automation Project 3 - Used Car Search
1. Navigate to http://www.cargurus.com
2. Click on Buy Use
3. Verify that the default selected option in Makes dropdown is All Makes (Use Assert 
methods for all verifications)
4. In Makes dropdown, choose Lamborghini
5. Verify that the default selected option in Models dropdown is All Models
6. Verify that Models dropdown options are [All Models, Aventador, Huracan, Urus, 
400GT, Centenario, Countach, Diablo, Espada, Gallardo, Murcielago]
7. In Models dropdown, choose Gallardo
8. Enter 22031 for zip and hit search
9. In the results page, verify that there are 15 search results, excluding the first sponsored 
result
To isolate the 15 results, excluding the first sponsored one, you can use a custom xpath
which excludes the first result. For example:
//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]
(find all <a> elements that have a common data-cg-ft attribute 'car-blade-link' (matches 
16 results) and whose href attribute does not contain text 'FEATURED')
10. Verify that all 15 result's title text contains "Lamborghini Gallardo"
11. From the dropdown on the left corner choose “Lowest price first” option and verify that 
all 15 results are sorted from lowest to highest. You should exclude the first result since 
it will not be a part of sorting logic.
To verify correct sorting, collect all 15 prices into a list, create a copy of it and sort in 
ascending order and check the equality of the sorted copy with the original.
12. From the dropdown menu, choose “Highest mileage first” option and verify that all 15 
results are sorted from highest to lowest. You should exclude the first result since it will 
not be a part of sorting logic.
13. On the left menu, click on Coupe AWD checkbox and verify that all results on the page 
contains “Coupe AWD”
14. Click on the last result (get the last result dynamically, i.e., your code should click on the 
last result regardless of how many results are there)
15. Once you are in the result details page go back to the results page and verify that the 
clicked result has “Viewed” text on it.

Remember: 90% of the exceptions and issues that you encounter can be resolved by 
synchronizing your code, i.e., by observing what is your code trying to do and what is 
actually happening on the UI and adjust your code by adding necessary waits. Make sure 
to set an implicit wait and use Thread.sleep() as needed.
