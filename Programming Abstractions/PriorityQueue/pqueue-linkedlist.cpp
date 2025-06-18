/*************************************************************
 * File: pqueue-linkedlist.cpp
 *
 * Implementation file for the LinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-linkedlist.h"
#include "error.h"
#include <iostream>
using namespace std;

//the constructor sets the size of priority queue to 0 and the head of the linkes list to NULL
LinkedListPriorityQueue::LinkedListPriorityQueue() {
	head=NULL;
	sizeQ=0;
}

//destructor destructs the Linked list
LinkedListPriorityQueue::~LinkedListPriorityQueue() {
		ListNode *cp = head;
		while (cp != NULL) {
			ListNode *next = cp->next;
			delete cp;
			cp = next;
		}
}

//this method returns the size of our priority queue
int LinkedListPriorityQueue::size() {
	return sizeQ;
}

//this boolean return if the priority queue is empty or not
bool LinkedListPriorityQueue::isEmpty() {
	return sizeQ==0;
}

//this void adds the string to priority queue
//(the method goes through the linked list and adds the string into the list)
void LinkedListPriorityQueue::enqueue(string value) {
	ListNode* nd = new ListNode;
	nd->val=value;

	ListNode** currentNode = &head;
	while((*currentNode) != NULL && (*currentNode)-> val < value ) {
		currentNode=&((*currentNode)->next);
	}
	nd->next=*currentNode;
	*currentNode=nd;

	sizeQ++;
}

//this string returns the most priority string in our priority queue without deleting it 
string LinkedListPriorityQueue::peek() {
	if(sizeQ!=0)
		return head->val;
	else 
		error("the Priority Queue is empty");
}

//this string returns the most priority string in our priority queue and deletes it
string LinkedListPriorityQueue::dequeueMin() {
	if(head!=NULL) {
		ListNode *copy = head;
		head=head->next;
		string answ = copy->val;
		sizeQ--;
		delete copy;
		return answ;
	}else 
		error("the Priority Queue is empty");
}

