using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <cstring>

struct kp {
  const void* key;
  const  void* val;
}K;


const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory) {
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

bool imdb::good() const {
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

 
int compare1(const void * p1, const void * p2) {
  return strcmp(*(char**)p1, *((char**)p1 + 1) + *(int*)p2);
}

int compare2(const void * f1, const void * f2) {
  film m1 = **(film**)f1;
  char* ptr = *((char**)f1 + 1) + *(int*)f2;
  
  film m2;
  m2.title = ptr;
  m2.year =  1900 + *(ptr+(strlen(ptr)+1));
 
  if(m1 == m2) return 0;
  else if(m1 < m2) return -1;
  else return 1;
}


short getN(char* & offset, int sz) {

  offset+=sz;
  if(sz%2!=0){
    sz++;
    offset+=1;
  }
  short n = *(short*)offset;
  offset+=2;
  if ((sz+2)%4!=0)
    offset+=2;
  
  return n;
}


int* findsmth(const void* vd, const void* file, int (*cmp)(const void*,const void*)) {
  K.key = vd;
  K.val = file;
  int* f = (int*) bsearch((void*)&K, (void*)((char*)file + sizeof(int)), (size_t)*(int*)file, sizeof(int), cmp);
  return f;
}

// you should be implementing these two methods right here... 
bool imdb::getCredits(const string& player, vector<film>& films) const  {
  int* find = findsmth((void*)player.c_str(), actorFile, compare1);
  
  if(find!=NULL){     
    char* offset = (char*)actorFile + *find;
    short nFilm = getN(offset, strlen(offset)+1);
    for (int j =0; j<nFilm;j++){
      char * pt = (char*)movieFile + *((int*)offset + j);
      film f;
      f.title = pt;
      f.year =  1900 + *(pt+(strlen(pt)+1));
      films.push_back(f);
    }	
    return true;
  }  
 return false;
}


bool imdb::getCast(const film& movie, vector<string>& players) const {
  const film* pt = &movie; 
  int* find = findsmth((void*)pt, movieFile,compare2);

  if(find!=NULL){   
    char* offset = (char*)movieFile + *find;
    short nPlayers = getN(offset, movie.title.size()+2);
      
    for(int j = 0; j < nPlayers;j++){
      char * playerP = (char*)actorFile + *((int*)offset + j);
      players.push_back(playerP);
    }
      return true;
  }
  return false;
}



imdb::~imdb(){
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}