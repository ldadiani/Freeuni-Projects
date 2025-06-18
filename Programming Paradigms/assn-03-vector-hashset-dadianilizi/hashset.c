#include "hashset.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <search.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn)
{
	assert(elemSize>0);
	assert(numBuckets>0);
	assert(hashfn !=NULL);
	assert(comparefn !=NULL);
	h->elem_num = 0;
	h->v_num = numBuckets;
	h->elem_size = elemSize;
	h->compfn = comparefn;
	h->hashfn= hashfn;
	h->freefn = freefn;
	h->v = (vector*)malloc(sizeof(vector)*h->v_num);
	assert(h->v);
	for(int i=0;i<numBuckets;i++)
		VectorNew(h->v+i,elemSize,freefn,4);
}

void HashSetDispose(hashset *h)
{
	if(h->freefn !=NULL){
		for(int i=0;i<h->v_num;i++)	
			VectorMap(h->v+i, h->freefn, NULL);
	}
	for(int i=0;i<h->v_num;i++)	
		VectorDispose(h->v+i);	
	free(h->v);
}

int HashSetCount(const hashset *h)
{ return h->elem_num; }

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData)
{
	assert(mapfn !=NULL);
	for(int i=0;i<h->v_num;i++){
		VectorMap(h->v+i, mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr)
{
	assert(elemAddr !=NULL);
	void* result = HashSetLookup(h, elemAddr);
	if(result != NULL){
		memcpy(result, elemAddr, h->elem_num);
	} else{
		int bucket = h->hashfn(elemAddr, h->v_num);	
		assert(h->v_num>bucket);
		assert(bucket>=0);
		VectorAppend(h->v+bucket, elemAddr);	
		h->elem_num++;	
	}
}

void *HashSetLookup(const hashset *h, const void *elemAddr)
{ 
	assert(elemAddr !=NULL);	
	int s = h->hashfn(elemAddr, h->v_num);	
	assert(s>=0);
	assert(h->v_num>s);	
	int result = VectorSearch(h->v+s, elemAddr, h->compfn, 0, false);
	if(result != -1) 
		return VectorNth(h->v+s, result);
	else return NULL;
 }
