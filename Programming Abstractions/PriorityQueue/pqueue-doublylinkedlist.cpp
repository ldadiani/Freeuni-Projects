/*************************************************************
 * File: pqueue-doublylinkedlist.cpp
 *
 * Implementation file for the DoublyLinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-doublylinkedlist.h"
#include "error.h"

//constructor
DoublyLinkedListPriorityQueue::DoublyLinkedListPriorityQueue() {
	sizeQ=0;
	head=NULL;
}

//destructor
DoublyLinkedListPriorityQueue::~DoublyLinkedListPriorityQueue() {
	ListNode *h=head;
	while(h!=NULL) {
		ListNode *node=h;
		h=h->next;
		delete node;
	}
}

//this method returns the size of priority queue
int DoublyLinkedListPriorityQueue::size() {
	return sizeQ;
}

//this bool return if the queue is empty or not
bool DoublyLinkedListPriorityQueue::isEmpty() {
	return sizeQ==0;
}


//this void adds the string to priority queue
void DoublyLinkedListPriorityQueue::enqueue(string value) {
	sizeQ++;


	ListNode *node = new ListNode;
	node-> val = value;
	node->back=NULL;
	if(head==NULL) {
		node->next = NULL;
	} else {
		node->next=head;
		node->next->back=node;
	}
	head=node;
}

//this string returns the most priority string in our priority queue without deleting it 
string DoublyLinkedListPriorityQueue::peek() {
	if(!isEmpty()) {
		ListNode *node = head;
		string ans=node->val;

		while(node!=NULL) {
			if (node->val < ans) {
				ans = node->val;
			}
			node = node->next;
		}
		return ans;

	}else {
		error("the Priority Queue is Empty");
	}
}

//this string returns the most priority string in our priority queue and deletes it
//(it goes through the linked list, searches peek element and then connects the nodes as neccessary)
string DoublyLinkedListPriorityQueue::dequeueMin() {
	if(!isEmpty()) {
		sizeQ--;

		string ans = peek();

		ListNode *node = head;
		while(node->val!=ans) {
			node=node->next;
		}

		if (node->next != NULL && node->back == NULL) {
			head = node->next;
			head->back = NULL;
		}else if (node->next == NULL && node->back == NULL) {
			head = NULL;
		} else if (node->next == NULL && node->back != NULL) {
			node->back->next = NULL;
		}  else {
			node->back->next = node->next;
			node->next->back = node->back;
		}
		delete node;
		return ans;
	}else {
		error("the Priority Queue is Empty");
	}
}

