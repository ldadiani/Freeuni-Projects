/******************************************************************************
 * File: Trailblazer.cpp
 *
 * Implementation of the graph algorithms that comprise the Trailblazer
 * assignment.
 */

#include "Trailblazer.h"
#include "TrailblazerGraphics.h"
#include "TrailblazerTypes.h"
#include "TrailblazerPQueue.h"
#include "random.h"

using namespace std;

/* Function: shortestPath
 * 
 * Finds the shortest path between the locations given by start and end in the
 * specified world.	 The cost of moving from one edge to the next is specified
 * by the given cost function.	The resulting path is then returned as a
 * Vector<Loc> containing the locations to visit in the order in which they
 * would be visited.	If no path is found, this function should report an
 * error.
 *
 * In Part Two of this assignment, you will need to add an additional parameter
 * to this function that represents the heuristic to use while performing the
 * search.  Make sure to update both this implementation prototype and the
 * function prototype in Trailblazer.h.
 */
Vector<Loc>
shortestPath(Loc start,
             Loc end,
             Grid<double>& world,
             double costFn(Loc from, Loc to, Grid<double>& world)
			 ,double heuristic(Loc start, Loc end, Grid<double>& world)) {

	Grid<info> infoGrid(world.numRows(),world.numCols());
	
	findPath(start,end,world,costFn,infoGrid, heuristic);


	Vector<Loc> reversed;
    while (end != start) {
        reversed.push_back(end);
        end = infoGrid[end.row][end.col].prev;
    }
	reversed.push_back(start);

    Vector<Loc> ans;
    for (int i = reversed.size() - 1; i >= 0; i--) {
        ans.push_back(reversed[i]);
    }

    return ans;
}



void findPath(Loc start,
             Loc end,
             Grid<double>& world,
			 double costFn(Loc from, Loc to, Grid<double>& world),Grid<info>& infoGrid,
			 double heuristic(Loc start, Loc end, Grid<double>& world)) {


	for (int i = 0; i < infoGrid.numRows(); i++) {
        for (int j = 0; j < infoGrid.numCols(); j++) {
            infoGrid[i][j].color = GRAY;
            infoGrid[i][j].cost = 0;
        }
    }

	TrailblazerPQueue< Loc > PQ;
	PQ.enqueue( start,0 );

	Loc l = start;

	while(!PQ.isEmpty()) {
		
		l = PQ.dequeueMin();
		colorCell(world,l,GREEN);
		
		if(l==end) return;

		for(int i = -1; i<=1; i++) {
			for(int j = -1; j<=1; j++ ) {

				Loc lc = makeLoc(l.row + i, l.col + j);

				if (world.inBounds(lc.row, lc.col) && lc != l) {
					
					if(infoGrid[lc.row][lc.col].color ==GRAY) {

						infoGrid[lc.row][lc.col].color=YELLOW;
						colorCell(world,lc,YELLOW);
						infoGrid[lc.row][lc.col].cost = infoGrid[l.row][l.col].cost + costFn(l,lc,world);
						infoGrid[lc.row][lc.col].prev = l;
						PQ.enqueue(lc,infoGrid[l.row][l.col].cost + costFn(l,lc,world) + heuristic(lc,end,world) );

					}

					if(infoGrid[lc.row][lc.col].color == YELLOW&&infoGrid[lc.row][lc.col].cost > 
						costFn(l,lc,world) + infoGrid[l.row][l.col].cost ) {

						infoGrid[lc.row][lc.col].cost = costFn(l,lc,world) + infoGrid[l.row][l.col].cost;
						infoGrid[lc.row][lc.col].prev = l;
						PQ.decreaseKey( lc, costFn(l,lc,world) + infoGrid[l.row][l.col].cost + heuristic(lc,end,world)  );

					}
				}
			}
		}
	}
}




/*this finctions creates a maze */
/*first we create a set with all the adges , then we take one and see if we already 
have visited the start location of the edge and the  end location, 
if we havent visited any of them we add edge to the set */
Set<Edge> createMaze(int numRows, int numCols) {
	Set<Edge> edges;
	
	for(int i = 0; i < numRows; i++) {
		for(int j = 0; j < numCols; j++) {
			
			Loc from = makeLoc(i,j);
			Loc right = makeLoc(i,j+1);
			Loc down = makeLoc(i+1,j);

			if( right.col>=0 && right.col<numCols && right.row>=0 && right.row<numRows ) 
					edges.add(makeEdge(from,right));

			if( down.col>=0 && down.col<numCols && down.row>=0 && down.row<numRows ) 
					edges.add(makeEdge(from,down));
		}
	}

	TrailblazerPQueue<Edge> PQ;
    foreach (Edge edge in edges) {
        PQ.enqueue(edge, randomInteger(0, 10));
    }

	Set<Edge> tree;
	Set<Loc> endpoints;
	while(!PQ.isEmpty()) {
		Edge e = PQ.dequeueMin();
		if(!endpoints.contains(e.start)||
			!endpoints.contains(e.end) ) {
			endpoints.add(e.start);
			tree.add(e);
		}
	}

    return tree;
}
