package ais;

public class MyRandom {
	long seed;
	long iseed;
	long multiplier;
	long addend;
	long mask;

	int BITS_PER_BYTE;
    int BYTES_PER_INT;

	double nextNextGaussian;
    boolean haveNextNextGaussian;
    
	boolean nextFlip(double probability)
	{
		seed = (seed * multiplier + addend) & mask;
	    return ((seed / (double)mask) <= probability);
	}
	
	MyRandom()
	{
		multiplier = 0x5DEECE66DL;
		addend = 0xBL;
		mask = 1;
		mask <<= 48;
		mask--;

		BITS_PER_BYTE = 8;
		BYTES_PER_INT = 4;

		haveNextNextGaussian = false;

		iseed = System.currentTimeMillis()/1000;
		setSeed(iseed);
	}


	MyRandom(long seed) 
	{
		multiplier = 0x5DEECE66DL;
		addend = 0xBL;
		mask = 1;
		mask <<= 48;
		mask--;

		BITS_PER_BYTE = 8;
		BYTES_PER_INT = 4;

		haveNextNextGaussian = false;

		iseed = seed;
		setSeed(seed);
	}
	void setSeed(long nseed) 
	{
		seed = (nseed ^ multiplier) & mask;
	    haveNextNextGaussian = false;
	}
	int nextInt(int LBound, int UBound)
	{
		return (LBound > UBound) ? LBound : (int)((nextDouble() * (UBound - LBound)) + LBound);
	}
	double nextDouble()
	{
	    long l = ((long)(next(26)) << 27) + next(27);
	    return l / (double)((long)1 << 53);
	}
	
	double nextGaussian() 
	{
		// See Knuth, ACP, Section 3.4.1 Algorithm C.
	    if (haveNextNextGaussian) 
		{
	    	haveNextNextGaussian = false;
	    	return nextNextGaussian;
	    } 
		else 
		{
			double v1, v2, s;
			do 
			{
				v1 = 2 * nextDouble() - 1; 
	            v2 = 2 * nextDouble() - 1; 
	            s = v1 * v1 + v2 * v2;
	    	} 
			while (s >= 1 || s == 0);

	    	double multiplier = sqrt(-2 * log(s)/s);
	    	nextNextGaussian = v2 * multiplier;
	    	haveNextNextGaussian = true;
	    	return v1 * multiplier;
	    }
	}
	int next(int bits)
	{
		seed = (seed * multiplier + addend) & mask;
		return (int)(seed >> (48 - bits));
	}
	
	int nextInt()
	{  
		return next(32); 
	}
	
	int nextInt(int n)
	{
	    if (n <= 0)
	        return 0;
		

	    if ((n & -n) == n)
	        return (int)((n * (long)next(31)) >> 31);

	    int bits, val;
	    do 
		{
	        bits = next(31);
	        val = bits % n;
	    } 
		while( ( bits - val + n - 1 ) < 0);
	    return val;
	}


}
