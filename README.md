# Queuing-System-Simulation
This is a simulation of how customers in a supermarket queue are being served by cashiers (servers).
Upon arrival, customers would be served by the first available server.
If all servers are unavailable, the customer would be allowed to queue at the nearest server, as long as the number of customers in that queue is less than the maximum number in queue, which is determined by user input.
If the number of customers in that queue reaches the maximum number in queue, the customer leaves the supermarket.

Servers are split into 2 categories, human servers and self-checkout counters.
Human servers are allowed to take occasional breaks. 
When a server finishes serving a customer, there is a chance that the server takes a rest for a certain amount of time.
The probability of rest is determined by user input.
During the break, the server does not serve the next waiting customer. 
Upon returning from the break, the server serves the next customer in the queue (if any) immediately.
Self-checkout counters, on the other hand, act just like a human server, but with no breaks.

Below is a sample run of the programme.

![image](https://github.com/user-attachments/assets/06d5d9d8-bf64-4e59-b811-335895152b3e)
