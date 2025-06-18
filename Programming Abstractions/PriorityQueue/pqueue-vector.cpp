/*************************************************************
 * File: pqueue-vector.cpp
 *
 * Implementation file for the VectorPriorityQueue
 * class.
 */
 
#include "pqueue-vector.h"
#include "error.h"
#include <vector>
#include <algorithm>

//vector already has a costructor and destructor, so we can leave the functions empty
VectorPriorityQueue::VectorPriorityQueue() {
	
}

VectorPriorityQueue::~VectorPriorityQueue() {
	
}

//this method returns the size of our priority queue
int VectorPriorityQueue::size() {
	return priorQ.size();
}

//this boolean return if the priority queue is empty or not
bool VectorPriorityQueue::isEmpty() {
	return priorQ.size()==0;
}

//this void adds the string to priority queue
void VectorPriorityQueue::enqueue(string value) {
	needs=true;
	priorQ.push_back(value);
}

//this string returns the most priority string in our priority queue without deleting it 
string VectorPriorityQueue::peek() {
	if(priorQ.size()!=0) { 
		if(needs)
			sort(priorQ.begin(),priorQ.end());
		return priorQ[0];
	}else 
		error("the Priority Queue is empty");
}


//this string returns the most priority string in our priority queue and deletes it
string VectorPriorityQueue::dequeueMin() {
	if(priorQ.size()!=0) {
		if(needs)
			sort(priorQ.begin(),priorQ.end());
		needs=false;
		string ans= priorQ[0];
		priorQ.erase(priorQ.begin());
		return ans;
	} else 
		error("the Priority Queue is empty");

}
