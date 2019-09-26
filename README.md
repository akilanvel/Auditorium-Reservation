# Auditorium-Reservation

This project reads from a file (that the user enters) containing an auditorium and determines which seats are available. Then, a user can 
request to reserve a seat or seats. The user enters how many adult tickets, senior tickets, and child tickets to reserve. If the seats are 
available, they will be reserved. If not, the program will recommend available seats nearest to the middle of the row. The user repeatedly
request to reserve until satisfied. 

Afterwards, the program reads through all auditorium files in the folder, and determines how many adult tickets, senior tickets, and child
tickets have been sold. It outputs the total revenue generated and seats reserved. Adult tickets cost $10, senior tickets cost $7.50, and 
child tickets cost $5.

The file(s) containing an auditorium must be formatted like this:
A....C..S
SSS......
...AAA...

A represents adult, S represents senior, and C represents child. Each '.' represents an available seat in the auditorium. The auditorium 
above has 3 rows and 9 columns. Each auditorium in the theater (folder) can be of different size. 
