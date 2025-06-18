/*
 * File: Boggle.cpp
 * ----------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the main starter file for Assignment #4, Boggle.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "gboggle.h"
#include "grid.h"
#include "gwindow.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
#include "vector.h"
#include "map.h"
#include "set.h"
using namespace std;

/* Constants */

const int BOGGLE_WINDOW_WIDTH = 650;
const int BOGGLE_WINDOW_HEIGHT = 350;
const int BIG_BOGGLE_SIZE=5;
const int REG_BOGGLE_SIZE=4;
const int MIN_WORD_SIZE=4;

const string STANDARD_CUBES[16]  = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
 
const string BIG_BOGGLE_CUBES[25]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

/* Function prototypes */
void welcome();
void giveInstructions();

//this function asks if the user needs the instructions
bool needsInstructions() {
	string ans;
	while(true) {
		cout<<"Do you need instructions? ";
		getline(cin,ans);
		if(toLowerCase(ans)=="yes") {
			return true;
		}
		if(toLowerCase(ans)=="no") {
			return false;
		}
		cout<<"Please answer yes or no"<< endl;
	}

}

//this void asks the board size
void askBoardSize(int& boardSize) {
	string ans;
	cout<< endl;
	cout<<"You can choose standart Boggle(4x4 grid)"<< endl;
	cout<<"or Big Boggle(5x5)"<< endl;

	while(true) {
		cout<<"Would you like a big boggle? ";
		getline(cin,ans);
		if(toLowerCase(ans)=="yes") {
			boardSize=BIG_BOGGLE_SIZE;
			break;
		}
		if(toLowerCase(ans)=="no") {
			boardSize=REG_BOGGLE_SIZE;
			break;
		}

		cout<<"Please answer yes or no"<< endl;

	}
}


//this is the void for user to generate the string
void userGeneratesString(int& boardSize , Grid<int>& letters ) {
	cout<<"Enter a "<<boardSize*boardSize<<"- character string to ";
	cout<<"identify which letters you want on cubes."<< endl;
	cout<<"The first "<<boardSize<<" letters are the cubes on the top ";
	cout<<"row from the left to right, the next "<<boardSize<<" letters ";
	cout<<"are the second row , and so on."<< endl;
	cout<<"Enter the string: ";
	string chars;
	while(true) {
		getline(cin,chars);
		if(chars.length()>= boardSize*boardSize) {
			chars= toUpperCase(chars);
			break;
		}
		cout<<"String must include "<<boardSize*boardSize<<" characters! Please try again: ";
	}

	
	for(int i = 0; i<boardSize*boardSize; i++) {
		int row = i/boardSize;
		int col = i% boardSize;
		letters[row][col]=chars[i];
		labelCube(row,col,chars[i]);

	}
}


//this void copies the standart masive into the vector
void copyStantartMass(Vector<string>& vecCopy) {
	for(int i = 0; i< REG_BOGGLE_SIZE*REG_BOGGLE_SIZE; i++) {
		vecCopy.add(STANDARD_CUBES[i]);
	}
}

//this void copies big massive into the vector
void copyBigMass(Vector<string>& vecCopy) {
	for(int i = 0; i< BIG_BOGGLE_SIZE*BIG_BOGGLE_SIZE; i++) {
		vecCopy.add(BIG_BOGGLE_CUBES[i]);
	}
}


//this is the void of generating string fro computer
void computerGeneratesString(int& boardSize, Grid<int>& letters ) {
	Vector<string> vecCopy;
	if(boardSize==REG_BOGGLE_SIZE) {
		copyStantartMass(vecCopy);
	} else {
		copyBigMass(vecCopy);
	}

	for(int i = 0; i< vecCopy.size(); i++) {
		int x = randomInteger(i, vecCopy.size()-1);
		string cop = vecCopy[i];
		vecCopy[i]=vecCopy[x];
		vecCopy[x]=cop;
	}

	for(int i = 0; i<boardSize*boardSize; i++) {
		int row = i/boardSize;
		int col = i% boardSize;
		int randEl=randomInteger(0,vecCopy[i].length()-1);
		letters[row][col]=vecCopy[i][randEl];
		labelCube(row,col,vecCopy[i][randEl]);
	}

}



//this void sets the generated chars into the board
void setBoardChars(int& boardSize, Grid<int>& letters ) {
	cout<< endl;
	cout<<"I'll give you  a chance to set up the board to your ";
	cout<<"specification , which makes it easier to confirm your ";
	cout<<"boggle program is working. "<< endl;
	
	bool force;
	string ans;

	while(true) {
		cout<<"Do you want to force the board configuration? ";
		getline(cin,ans);
		if(toLowerCase(ans)=="yes") {
			force= true;
			break;
		}
		if(toLowerCase(ans)=="no") {
			force= false;
			break;
		}

		cout<<"Please answer yes or no"<< endl;
	}

	if(force) {
		userGeneratesString(boardSize,letters);
	} else {
		computerGeneratesString(boardSize,letters);
	}

}

//this void resets boolean grid to all false
void reset (Grid<bool>& used) {
	for(int i = 0; i< used.nCols; i++) {
		for(int j = 0; j< used.nRows; j++) {
			used[i][j]=false;
		}
	}
}


//this void finds all the words with recursive function and saves them in vector and also in map with the key word and value its coordinates
void findWords(string pref, int i, int j , Lexicon& lex,Map <string,Vector<pair<int,int> > >& wordsWcoordinates,Vector<string>& words, 
			   int& boardSize,Grid<int>& letters,Grid<bool> used,Vector< pair<int,int> > coordinates,Set<string>& set) {
		
	if (i>=boardSize||i<0||j>=boardSize||j<0) {
		return;
	}
	if(lex.contains(pref)&& pref.size()>=MIN_WORD_SIZE)  {
		pref=toLowerCase(pref);
		int n = set.size();
		set.insert(pref);
		if(set.size()>n) {
			words.add(pref);
			wordsWcoordinates[pref]=coordinates;
		}
	}
	
	if(!lex.containsPrefix(pref)) {
		return;
	}
	if(used[i][j]){
		return;
	}
	

	pref+=letters[i][j];
	used[i][j]=true;
	coordinates.push_back(pair<int,int>(i,j));

	for (int col = -1; col <= 1; col++) {
        for (int row = -1; row <= 1; row++) {
            findWords(pref, i + col, j + row,lex, wordsWcoordinates,
                            words, boardSize, letters, used,
                             coordinates, set);
        }
    }
}



//this void finds all the words from all the chars in the grid
void findAllWords(Lexicon& lex,Map <string,Vector< pair<int,int> > >& wordsWcoordinates,Vector<string>& words, int& boardSize,
				  Grid<int>& letters,Grid<bool> used, Vector< pair<int,int> >& coordinates, Set<string>& set) {
	for(int i = 0; i<boardSize; i++) {
		for (int j = 0; j<boardSize; j++) {
			string pref;
			Vector< pair<int,int> > coordinates;
			findWords(pref,i,j,lex,wordsWcoordinates,words,boardSize,letters,used,coordinates,set);
		}
	}	
}


//this is the boolean if vector contains a word
bool contains(Vector<string>& words, string& w) {
	for(int i = 0; i < words.size(); i++) {
		if(words[i]==w) return true;
	}

	return false;
}

//this void highlights the word that user found
void highlight(string& w,Map <string,Vector< pair<int,int> > >& wordsWcoordinates) {
	for(int i = 0 ; i < wordsWcoordinates[w].size(); i++) {
		highlightCube(wordsWcoordinates[w][i].first,wordsWcoordinates[w][i].second,true);
	}
	pause(800);
	for(int i = 0 ; i < wordsWcoordinates[w].size(); i++) {
		highlightCube(wordsWcoordinates[w][i].first,wordsWcoordinates[w][i].second,false);
	}
}

//this void removes string from the vector
void remove(Vector<string>& words, string& w) {
	for(int i =0; i< words.size(); i++ ){
		if(words[i]==w){
			words.remove(i);
			break;
		}
	}
}


//this void is for user to play the turn
void userTurn(Map <string,Vector< pair<int,int> > >& wordsWcoordinates,Vector<string>& words,Lexicon& lex,Vector<string>& guessed) {
	cout<<"Ok, take all the time you want and find all the words you can!";
	cout<<"Signal that you're finished by entering an empty line."<< endl<< endl;
	string w;
	while(true) {
		cout<< "Enter a Word: ";
		getline(cin,w);
		w = toLowerCase(w);
		if(w.size()==0) break;
		else if(w.length()<MIN_WORD_SIZE) 
			cout<<"That word doesn't meet the minimum word length."<< endl;
		else if(!lex.contains(w)) 
			cout<<"Thats not a word!"<< endl;
		else if (contains(guessed,w))
			cout<<"You've already guessed that!"<< endl;
		else if (!contains(words,w)) 
			cout<<"You can't make that word."<< endl;
		else {
			highlight(w,wordsWcoordinates);
			wordsWcoordinates.remove(w);
			remove(words,w);
			guessed.add(w);
			recordWordForPlayer(w,HUMAN);
		}
	}
}


//this void is computers turn
//it gives all the words that user did not 
void computerTurn(Vector<string>& words) {
	for(int i = 0; i< words.size(); i++ ){
		recordWordForPlayer(words[i],COMPUTER);
	}

}


//this void is used to start playing
void play(Map <string,Vector< pair<int,int> > >& wordsWcoordinates,Vector<string>& words,Lexicon& lex) {
		Vector<string> guessed;
		userTurn(wordsWcoordinates,words,lex,guessed);
		computerTurn(words);
}


//this function asks if the user wants to play again
bool playAgain () {
	string ans;
	while(true) {
		cout<<"Would you like to play again? ";
		getline(cin,ans);
		if(toLowerCase(ans)=="no") return false;
		else if (toLowerCase(ans)=="yes") return true;
		else cout<<"Please enter yes or no "<< endl;
	}
}

//this is the main function which runs the game
int main() {
    GWindow gw(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    initGBoggle(gw);
	Lexicon lex("EnglishWords.dat");

    welcome();
	if(needsInstructions()) {
		giveInstructions();
	}
	int boardSize=0;
	askBoardSize(boardSize);

	
	bool playAg=true;
	while(playAg) {
		drawBoard(boardSize,boardSize);
		Grid<int> letters (boardSize,boardSize);
		setBoardChars(boardSize, letters);

		Set<string> set;
		Grid<bool> used (boardSize,boardSize);
		Map <string,Vector< pair<int,int> > > wordsWcoordinates;
		Vector<string> words;
		Vector< pair<int,int> > coordinates;
		findAllWords(lex,wordsWcoordinates,words, boardSize , letters,used,coordinates,set);
			cout<<words.toString()<< endl;
		//	cout<<words.size()<< endl;
		play(wordsWcoordinates,words,lex);

		playAg = playAgain();
		if(!playAg) break;
	}
    return 0;
}

/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */

void welcome() {
    cout << "Welcome!  You're about to play an intense game ";
    cout << "of mind-numbing Boggle.  The good news is that ";
    cout << "you might improve your vocabulary a bit.  The ";
    cout << "bad news is that you're probably going to lose ";
    cout << "miserably to this little dictionary-toting hunk ";
    cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}

/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */

void giveInstructions() {
    cout << endl;
    cout << "The boggle board is a grid onto which I ";
    cout << "I will randomly distribute cubes. These ";
    cout << "6-sided cubes have letters rather than ";
    cout << "numbers on the faces, creating a grid of ";
    cout << "letters on which you try to form words. ";
    cout << "You go first, entering all the words you can ";
    cout << "find that are formed by tracing adjoining ";
    cout << "letters. Two letters adjoin if they are next ";
    cout << "to each other horizontally, vertically, or ";
    cout << "diagonally. A letter can only be used once ";
    cout << "in each word. Words must be at least four ";
    cout << "letters long and can be counted only once. ";
    cout << "You score points based on word length: a ";
    cout << "4-letter word is worth 1 point, 5-letters ";
    cout << "earn 2 points, and so on. After your puny ";
    cout << "brain is exhausted, I, the supercomputer, ";
    cout << "will find all the remaining words and double ";
    cout << "or triple your paltry score." << endl << endl;
    cout << "Hit return when you're ready...";
    getLine();
}

