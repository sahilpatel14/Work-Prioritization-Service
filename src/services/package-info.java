package services;


/*

DATA STRUCTURES WE USE

In a gist, we are using

TreeSet : to store list of Jobs,
LinkedArrayList(Sorted) : to store list of Workers
ArrayList(Unsorted) : to store assigned Jobs for every worker

Internal sort method to that implements mergeSort for sorting our LinkedArrayList.


EXPLANATION :

There are two main entities that must be stored. Jobs And Workers. Both need to be sorted.

Jobs are supposed to be arranged in decreasing order of priority and
Workers are supposed to be arranged in increasing number of assigned tasks count.

The prime difference between the two lists is that Workers List will be changed dynamically.
i.e. since jobs will be assigned at run time, the ordering would changes at every job assignment.

Jobs will always be unique therefore a 'Set' is a good choice for storing them.

To maintain the Sorted order, we can use a self balancing tree such as AVL tree. In Java Collections API,
we get a construct called TreeSet which implements red black trees. We can use a TreeSet for storing
Jobs. (refer sample project)

TreeSet can not be used for storing Workers. Even though they are unique and can be represented as a set,
the sorting order we require is dynamic in nature. As per JavaDocs, If the sort order for a TreeSet can be changed at run time, we should avoid using a TreeSet. Sort order for Worker's list, i.e. # assignedJobs is mutable.

We can create a custom implementation of Red black tree or AVL trees to store Workers or use
a normal linked list and sort it after every job assignment.

I have used a linked list to store workers. The internal sort method in Collections API uses merge sort.
Therefore, for a partially sorted list, adding a new element and sorting would take less than O(nlogn) time.

Since we already have an upper limit to number of workers i.e. 5000 workers, we can use this approach without much concern.
In future, if the sort operation is causing a bottleneck for the whole service, we can also go for sorting list at particular intervals and only fetching minimum of list at every assignment.




There are three major concerns while scaling a service or an application.

Memory Useage
Processing Time
Crash or Error Recovery


Lets discuss Memory first.

In our work prioritization service, As the number of jobs coming from live stream increases,
storing them in main memory is not a viable option. Especially if our prioritization service
is not able to catch up with incoming job traffic. This would fill up our job lists and our service will
run out of memory.

It is equally true for Workers as well. We have not created any construct to
clear assigned jobs for a worker. This adds a finite limit to number of jobs that we can assign.
Exceeding this number would result into an OME.


Processing Time

At present, we have tapped the throughput value between two jobs in incoming data stream to 0.5 seconds.
While scaling the service, the throughput might reduce and fill out the assigned jobs buffer list entirely.
This would cause a backlog of jobs for our job assigner threads.

On the contrary, If the throughput from incoming jobs is too high, our job assigner threads would be kept waiting
for long. This would especially cause performance issues when traffic for one particular skill type is higher than
other skill types.


SOLUTION

DATA issues can be solved by storing jobs and workers in secondary storage. However, we will have to consider the
side effects of storing them in secondary storage first.

Database access calls are costly. They take significant amount of time. Since, our service requires updated Worker
and Job objects in every iteration, Database access calls might cause bottlenecking.

For storing Workers and their assigned tasks, we only require around 100 top workers having least # assigned jobs from the db. Therefore, we can store all workers in secondary storage with top 100 or 200 in our designated buffer Set in main memory.
Once, all the Workers inside the Set have been assigned a job, we swap the Set with a new one from db.


For storing Jobs, we can use a similar method with some additional filters. Like Workers, Jobs will be saved in a db.
Jobs with highest priority for every skill type will be fetched from db and saved in main memory. A decent set of 100 would do the job. (this arbitrary number could be made dynamic based on incoming job traffic).
For every new job that comes, we check if it can be placed in our job buffer or not. If yes, we put it in our jobs buffer. If not,
we move it into our db.


An SQL based RDBMS would be good for querying the data but it might cause issues with scaling and performance if not set up correctly. NoSQL based dbs like Mongo scale really well. We can use a NoSQL db to save our objects.



Processing Time :

Assigning jobs of a particular skill type can be considered as a separate independent task. Therfore, we can have threads running
parallelly for every skill type. This has been implemented in the sample project. Right now we are running the threads every 1 second for new Jobs. This number can be made dynamic depending on traffic rate of incoming jobs.

We can also try to run jobAssigner thread only when a new job of that type is received. However, this could also cause bottlenecking at job receiving end.

Therefore, for proper utilisation of processing power, we can have a better thread manangement wrapper for our Job Assigner threads. The wrapper would tune job assigners based on incoming job traffic.


Crash Recovery
Coming back from a dead state is another such challenge of scalability. If our service fails at any time, we need to make sure it gets restarted and assigned jobs are not lost. Using a secondary storage solves this problem to a large extent.





 */