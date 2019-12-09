# Parallel
The repository is dedicated to IPC subject, study for Master Degree first term.

In most of the packages the following scheme is implemented:


1. **threads_impl** package - implementation with JDK threds.
2. **rmi_impl** package - implementation with java RMI to achive inter-proccess communication. <br>
**NOTE!** This implementation doesn't seem to work great: It causes realy strong lags because of load to Server process.
3. **socket_impl** package - implementation with Java Sockets to achive inter-hosts communication. <br>

The rest of packages is intended to some studiyng:
1. **primitives** package - implementations of some synchroning primitives (e.g. BiSemaphore, just Semaphore, Channel and etc.). Some o them quite interesting.
2. **patterns** package - intended to Read-Write Lock and Consumers/Producers problem.
3. **utils** package - conatins Utility class that is helpful for demonstrating what happens under the hood.
4. **runners** package - conatins some usages of primitives and so on and so forth.
