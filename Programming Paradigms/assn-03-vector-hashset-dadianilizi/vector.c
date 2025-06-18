#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <search.h>


void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation){
	assert( elemSize>0);
	assert( initialAllocation>=0);
	if(initialAllocation==0)
		initialAllocation = 4;

	v->len = 0;
	v->elem_size = elemSize;
	v->memlen = initialAllocation;
	v->elems = malloc(elemSize*v->memlen);
	v->initialallocation = initialAllocation;

	assert(v->elems);

	v->freefn = freeFn;

}

void VectorDispose(vector *v){
	if(v->freefn !=NULL){
		for(int i=0;i<v->len;i++)
			v->freefn((char*)v->elems+i*v->elem_size);
	}
	free(v->elems);
}

int VectorLength(const vector *v)
{ return v->len; }

void *VectorNth(const vector *v, int position){
	assert(position>=0);
	assert(v->len>position);

	return (char*)v->elems+position*v->elem_size;
}

void VectorReplace(vector *v, const void *elemAddr, int position){
	assert(position>=0);		
	assert(v->len>position);

	memcpy(VectorNth(v,position), elemAddr, v->elem_size);
}

void VectorInsert(vector *v, const void *elemAddr, int position){
	assert(position>=0);                
	assert(v->len>=position);

	if ( v->len == v->memlen) {
		v->memlen += v->initialallocation;
		v->elems = realloc(v->elems, v->memlen*v->elem_size);
	}

	if (v->len == position)
		VectorAppend(v, elemAddr);
	else{
		int size =  v->elem_size*(v->len-position);
		char* front = VectorNth(v, position);
		char* back = front + v->elem_size;
		char* buf = (char*)malloc(size);
			
		memcpy(buf, front, size);
		memcpy(front, elemAddr, v->elem_size);	
		memcpy(back, buf, size);
		free(buf);
		v->len++;
	}
}

void VectorAppend(vector *v, const void *elemAddr)
{
	if (v->len == v->memlen) {
		v->memlen += v->initialallocation;
		v->elems = realloc(v->elems, v->memlen*v->elem_size);
	}

	v->len++;
	void *s = VectorNth(v,v->len-1);
	memcpy(s, elemAddr, v->elem_size);
}

void VectorDelete(vector *v, int position){
	assert(position>=0);
	assert(v->len>position);

	char *s = VectorNth(v,position);
	if(v->freefn !=NULL){
		v->freefn(s);
	}

	if(v->len-1 != position){  
		int end_size = v->len-position-1;
		for(int i=0;i<end_size;i++){
			char* next = s + v->elem_size;			
			memcpy(s, next, v->elem_size);
			s = next;
		}
	}
	v->len--;
}

void VectorSort(vector *v, VectorCompareFunction compare){
	assert(compare != NULL);
	qsort(v->elems, v->len, v->elem_size, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData){
	assert(mapFn != NULL);
	for(int i=0; i<v->len; i++){
		char *s = (char*)v->elems + i*v->elem_size;		
		mapFn(s, auxData);
	}
}


static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted){
	assert(startIndex>=0);
	assert(v->len>=startIndex);
	assert(key != NULL);
	assert(searchFn != NULL);

	char* ans;
	char* st = (char*)v->elems + startIndex*v->elem_size;
	if(isSorted)
		ans = bsearch(key, st, v->len-startIndex, v->elem_size, searchFn);
	else{
		size_t size = v->len-startIndex;
		ans = lfind(key, st, &size, v->elem_size, searchFn);
	}
	if(ans) {
		return (ans-(char*)v->elems)/v->elem_size;
	} else return kNotFound;
} 




