/*************************************************************
 * File: pqueue-heap.cpp
 *
 * Implementation file for the HeapPriorityQueue
 * class.
 */
 
#include "pqueue-heap.h"
#include "error.h"
#include <algorithm>

//constructor
HeapPriorityQueue::HeapPriorityQueue() {
	sizeQ = 0;
	reservoir = EXPAND;
    heap = new string[reservoir];
}

//destructor
HeapPriorityQueue::~HeapPriorityQueue() {
	delete[] heap;
}


//this method return the size of the queue
int HeapPriorityQueue::size() {
	return sizeQ;
}

//this bool returns if the queue is empty or not
bool HeapPriorityQueue::isEmpty() {
	return sizeQ==0;
}

//this method adds the element to the priority queue 
//(in massive it adds element to the end and then bubbles it up to its correct place)
void HeapPriorityQueue::enqueue(string value) {
	if(sizeQ+1==reservoir) expandSize();
	sizeQ++;
	heap[sizeQ]=value;
	bubbleUp(sizeQ);
}

//this method returns the most priority element without deleting it
string HeapPriorityQueue::peek() {
	if(sizeQ!=0) {
		return heap[first];
	} else
		error("the Priority Queue is Empty");
}


//this method returns the most priority element and deletes it
//it bubbles down the first element , and sizes down the sizeQ, so the element will not be in the queue no more)
string HeapPriorityQueue::dequeueMin() {
	if(sizeQ!=0) {
		string ans=heap[first];
		heap[first] = heap[sizeQ];
		sizeQ--;
		bubbleDown(first);
		return ans;
	} else
		error("the Priority Queue is Empty");
}

//if the massive is full this void expands the size by copying one massive into the bigger one
void HeapPriorityQueue::expandSize() {

	string* copy = heap;
	reservoir=2*reservoir;
	heap=new string[reservoir];
	
	for(int i=0; i<sizeQ; i++) {
		heap[i+1]=copy[i+1];
	}
	delete[] copy;
}


//this void bubbles up the new added string
//(it recplaces the parent and child massives and by 
//recursively using the finction string goes to the correct place)
void HeapPriorityQueue::bubbleUp(int n) {
	if(n<=1) return;
	int p = n/2;
	if(heap[p]>heap[n]) {
		string cp = heap[n];
		heap[n]=heap[p];
		heap[p]=cp;
		bubbleUp(p);
	}
}

 
//this void bubbles down the string when we want to delete it
//(first we choose the smaller priority child of the massive and we swap it with the given elemen
//if the massive has only one child we swap the child with the parent massive
//by recursion we do it until the string is bubbled down fully)
void HeapPriorityQueue:: bubbleDown(int n) {
	int firstChild = 2*n;
	int secondChild = firstChild+1;

	if(secondChild<=sizeQ) {
		int minInd;
			if(heap[firstChild]<=heap[secondChild]) minInd=firstChild;
			else minInd=secondChild;


		if(heap[minInd]<heap[n]) {
			string cp = heap[n];
			heap[n]=heap[minInd];
			heap[minInd]=cp;
			bubbleDown(minInd);
		}
	}

	if(firstChild<=sizeQ) {
		if(heap[n]>heap[firstChild]) {
			string cp = heap[n];
			heap[n]=heap[firstChild];
			heap[firstChild]=cp;
		}
	}

}