package gestures;

import game.Variables;
import gestures.filters.Filter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jbox2d.common.Vec2;
/**
 * The Trace is the class which represents a right drawing.
 * @author Quentin Bernard et Ludovic Feltz
 */

/* <This program is an Shoot Them up space game, called Escape-IR, made by IR students.>
 *  Copyright (C) <2012>  <BERNARD Quentin & FELTZ Ludovic>

 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
public class Trace {
	
	/**
	 * The number maximum of point that the trace accept.
	 */
	private static final int MAX_SIZE_LIST = 200;

	/**
	 * Our current trace, which are represents by a List of Vec2.
	 */
	private final List<Vec2> trace;
	
	/**
	 * A boolean, to stock if the trace is valid or not.
	 */
	private boolean valid;

	/**
	 * Default constructor
	 */
	public Trace() {
		trace = new LinkedList<>();
		valid = false;
	}
	
	/**
	 * Check if the current trace is empty or not.
	 * @return true if the current trace is empty, else false
	 */
	public boolean isEmpty(){
		return trace.isEmpty();
	}
	
	/**
	 * Return the current trace, which is represents by a List of Vec2.
	 * @return the current trace, which is represents by a List of Vec2
	 */
	public List<Vec2> getTrace() {
		return trace;
	}
	
	/**
	 * Add a vec2 to our current trace.
	 * @param vec - the vec2 to add to our current trace
	 */
	public void addPoint(Vec2 vec){
		if(trace.size()<= MAX_SIZE_LIST)
		trace.add(vec);
	}
	
	/**
	 * Check if a Trace is correctly recognized by our movement, defined by the interface Filter.
	 * All trace are managed By Gesture.
	 * @see Filter
	 * @see Gesture
	 */
	public boolean checkTrace(Filter filter) {
		if(filter.check(trace)){
			valid = true;
			return true;
		}
		return false;
	}

	/**
	 * Return true if the trace is correctly valid. Be careful, you have to call checkTrace before you call this methods, else no garrantly of results.
	 * @return true if the trace is valid, and false if not, but only if checkTrace has been called before this methods. Else, return always false
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Set the valid parameter of a trace
	 * @param valid - the boolean to set the current state of the trace.
	 */
	public void setValid(boolean valid) {//Only uses by the validation of weapon
		this.valid = valid;
	}


	
	/**
	 * Remove the lastPoint of trace, which is the first display by the player
	 */
	public void removeLastPoint(){
		if(trace.isEmpty())
			return;
		Random rand = new Random();// Removing the queue, point by point
		if(rand.nextInt()%Variables.TRACE_DELETE_RATE == 0)//RATE_DELETE_TRACE is the speed Rate for delete the trace
			trace.remove(0);
	}
	
	/**
	 * Display a Trace, which represents by a List of Vec2
	 * @param graphics - the graphics to draw on
	 */
	public void render(Graphics2D graphics){
		Iterator <Vec2> traceIte = trace.iterator();
		
		if(!traceIte.hasNext())
			return;
		
		Vec2 currentPoint = traceIte.next();
		Vec2 lastPoint = null;
		
		while(traceIte.hasNext()){
			lastPoint = currentPoint;
			currentPoint = traceIte.next();
			
			graphics.setStroke(new BasicStroke(4));// For have a bigest trace drawing
			graphics.drawLine((int)lastPoint.x, (int)lastPoint.y, (int)currentPoint.x, (int)currentPoint.y);//The line to trace
			
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.1f));//Draw a transparence line for effect
			graphics.setStroke(new BasicStroke(12));
			graphics.drawLine((int)lastPoint.x, (int)lastPoint.y, (int)currentPoint.x, (int)currentPoint.y);
		
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));//Remove the transparence effect
		}
		graphics.setStroke(new BasicStroke(1));//Remove the stroke effect
		}
}