/*
 *  java.lang.IllegalArgumentException
 *
 *  (c) 1997 George David Morrison
 *
 *  API version: 1.0.2
 *
 *  History:
 *  01JAN1997  George David Morrison
 *    Initial version
 */

package java.lang;

public class IllegalArgumentException extends RuntimeException
{
	public IllegalArgumentException() 
	{ 
		super(); 
	}

	public IllegalArgumentException(String s) 
	{ 
		super(s); 
	}
}

