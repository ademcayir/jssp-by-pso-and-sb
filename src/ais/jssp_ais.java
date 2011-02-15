import java.util.Random;




class Define {
	public static final String SP = " \\x0A";
}


class jssp_ais {

	int m = 0, n = 0;

	int size = 0;

	int job[][][] = null;

	int ag[][] = null;

	int ab[][] = null;

	int population = 0;

	int nag = 0;

class stime
{
	int start, finish;
	int duration;
	int lstart, lfinish;
	int lfloat;
	int job;
	int operation;
	int machine;
	stime next, prev;
	stime lnext, lprev;
}

class sstime
{
	int start, finish;
	sstime next, prev;
}

class pjob
{
	int job;
	int position;
}

class nnp
{
	int begin;
	int job;
}

pjob ma = null;				
Random rnd = null;

stime schedule[] = null;          //El final de cada m�quina
stime base[] = null;              //El principio de cada m�quina
stime jobs[] = null;              //El principio de cada trabajo

sstime lschedule = null;
sstime lbase = null;

int ntask[] ;
int ttask[] ;
int nmachine[] ;
int tmachine[] ;

stime operation[] = null;          //Las operaciones de la decodificaci�n normal
sstime loperation = null;        //Las operaciones de la evaluaci�n
nnp np[] = null;



void loadjssp ( String fname )
{
	FILE *fp;
	char line[128], title[128];
	int nline = 0;
	char *token;

	int i = 0, j = 0, k = 0;	

	if ( ( fp = fopen ( fname, "r" ) ) == null )
		return;

	while ( fgets ( line, 128, fp ) != null )
	{
		if ( nline == 0 )
			strcpy ( title, line + 1 );
		else if ( nline == 1 )
		{
			i = 0;

			token = strtok( line, SP );
			while( token != null )
			{
				( i ++ == 0 ) ? m = atoi ( token ) : n = atoi ( token );
				token = strtok ( null, SP );
			}

			size = m * n;

			job = new int **[m];

			for ( j = 0; j < m; j ++ )
			{
				job[j] = new int *[n];

				for ( k = 0; k < n; k ++ )
					job[j][k] = new int[2];
			}

			jobs = new struct stime *[ m ];

			i = j = k = 0;
		}
		else
		{
			int r = 0;

			token = strtok ( line, SP );
			while( token != null )
			{
				if ( j == n )
				{
					j = 0;
					k ++;
				}

				r = i % 2;
				job[k][j][r] = atoi( token );

				i ++;

				if ( r )
					j ++;

				token = strtok ( null, SP );
			}
		}
		nline ++;
	}

	fclose ( fp );

	//**********************************************************

	ma = new pjob *[ n ];
	int mach = 0;
	int *nm = new int [ n ];

	for ( i = 0; i < n; i++ )
	{
		ma[ i ] = new pjob[ m ];
		nm[ i ] = 0;
	}
	
	for ( i = 0; i < m; i ++ )
		for ( j = 0; j < n; j ++ )
		{
			mach = job[ i ][ j ][ 0 ];
			ma[ mach ][ nm[ mach ] ].job = i;
			ma[ mach ][ nm[ mach ] ].position = j;
			nm[ mach ]++;
		}
	delete nm;
	
	//**********************************************************
	// Inicializamos las variables para el c�lculo

	schedule = new stime *[ n ];
	base = new stime *[ n ];

	lschedule = new sstime *[ n ];
	lbase = new sstime *[ n ];

	ntask = new int [ m ];
	ttask = new int [ m ];
	
	nmachine = new int [ n ];
	tmachine = new int [ n ];

	operation = new stime[ size ];
	loperation = new sstime[ size ];

	np = new nnp * [ size ];

	for ( i = 0; i < size; i++ )
		np[ i ] = new nnp;
	//**********************************************************
}

int *loadsolution ( char *fname )
{
	FILE *fp;
	char *line = null;
	int nline = 0, i = 0, m = 0, n = 0, *sol = null, len = 0;
	char *token = null;


	if ( ( fp = fopen ( fname, "r" ) ) == null )
		return null;

	line = new char[128];

	while ( fgets ( line, (( nline == 0) ? 128 : len), fp ) != null )
	{
		if ( nline == 0 )
		{
			i = 0;

			token = strtok( line, SP );
			while( token != null )
			{
				( i ++ == 0 ) ? m = atoi ( token ) : n = atoi ( token );
				token = strtok ( null, SP );
			}

			delete line;

			len = m * n;
			sol = new int [ len ];

			len *= 4;
			line = new char[ len ];
			

			i = 0;
		}
		else if ( nline == 1 )
		{
			token = strtok ( line, SP );
			while( token != null )
			{
				sol[i++] = atoi( token );
				token = strtok ( null, SP );
			}
		}
		else
			break;
		nline ++;
	}

	for ( i = 0; i < m * n; i ++ )
		printf ( "%d ", sol[i] );

	putchar( '\n' );

	fclose ( fp );

	return sol;
}

void swap(int * a, int * b)
{
	register int t = * a;
	* a = * b;
	* b = t;
}


int evaluation ( int antiBody[] )
{
	int i;
	int makespan = 0;
	int cmachine, cjob, ctask, duration;
	int cnt = 0;
	sstime nschedule, cschedule;

	for ( i = 0 ; i < m ; i ++ )
		ntask [ i ] = ttask [ i ] = 0;

	for ( i = 0 ; i < n ; i ++ )
	{
		lschedule[ i ] = lbase[ i ] = null;
		tmachine [ i ] = nmachine [ i ] = 0;
	}

	for ( i = 0 ; i < size ; i ++ )
	{
		cjob = antiBody [ i ];
		ctask = ntask [ cjob ];
		cmachine = job[ cjob ][ ctask ][ 0 ];
		duration = job[ cjob ][ ctask ][ 1 ];

		if ( lschedule[ cmachine ] == null )
		{
			lbase[ cmachine ] = lschedule[ cmachine ] = loperation[ cnt ++ ];
			lschedule[ cmachine ].start = ttask[ cjob ] + 1;
			lschedule[ cmachine ].finish = ttask[ cjob ] = tmachine[ cmachine ] = ttask[ cjob ]  + duration;
			lschedule[ cmachine ].next = lschedule[ cmachine ].prev = null;
		}
		else
		{
			nschedule = loperation[ cnt ++ ];
			if ( ttask[ cjob ] >= lschedule[ cmachine ].finish )
			{
				nschedule.start = ttask[ cjob ] + 1;
				nschedule.finish = tmachine[ cmachine ] = ttask[ cjob ] = ttask[ cjob ] + duration;	
				lschedule[ cmachine ].next = nschedule;
				nschedule.prev = lschedule[ cmachine ];
				nschedule.next = null;
				lschedule[ cmachine ] = nschedule;
			}
			else
			{
				cschedule = lbase[ cmachine ];
				do
				{
					if ( cschedule.prev == null && cschedule.start > ( ttask[ cjob ] + duration ) )
					{
						nschedule.start = ttask[ cjob ] + 1;
						nschedule.finish = ttask[ cjob ] = ttask[ cjob ] + duration;
						cschedule.prev = nschedule;
						nschedule.next = cschedule;
						nschedule.prev = null;
						lbase[ cmachine ] = nschedule;
						break;
					}
					else if ( cschedule.next == null)
					{
						if ( cschedule.prev != null )
						{
							if ( duration <= ( cschedule.start - cschedule.prev.finish - 1 ) && ttask[ cjob ] < ( cschedule.start - duration ) )
							{
								if ( ttask[ cjob ] > cschedule.prev.finish )
								{
									nschedule.start = ttask[ cjob ] + 1;
									nschedule.finish = ttask[ cjob ] = ttask[ cjob ] + duration;
								}
								else
								{
									nschedule.start = cschedule.prev.finish + 1;
									nschedule.finish = ttask[ cjob ] = cschedule.prev.finish + duration;
								}

								cschedule.prev.next = nschedule;
								nschedule.prev = cschedule.prev;
								nschedule.next = cschedule;
								cschedule.prev = nschedule;
								break;
							}
						}
						
						if ( ttask[ cjob ] > tmachine [ cmachine ] )
						{
							nschedule.start = ttask[ cjob ] + 1;
							nschedule.finish = tmachine[ cmachine ] = ttask[ cjob ] = ttask[ cjob ] + duration;
						}
						else
						{	
							nschedule.start = tmachine[ cmachine ] + 1;
							nschedule.finish = tmachine[ cmachine ] = ttask[ cjob ] = tmachine[ cmachine ] + duration;
						}

						lschedule[ cmachine ].next = nschedule;
						nschedule.prev = lschedule[ cmachine ];
						nschedule.next = null;
						lschedule[ cmachine ] = nschedule;
						break;
					}
					else if ( cschedule.prev != null && cschedule.next != null )
					{
						if ( duration <= ( cschedule.start - cschedule.prev.finish - 1 ) && ttask[ cjob ] < ( cschedule.start - duration ) )
						{
							if ( ttask[ cjob ] > cschedule.prev.finish )
							{
								nschedule.start = ttask[ cjob ] + 1;
								nschedule.finish = ttask[ cjob ] = ttask[ cjob ] + duration;
							}
							else
							{
								nschedule.start = cschedule.prev.finish + 1;
								nschedule.finish = ttask[ cjob ] = cschedule.prev.finish + duration;
							}

							cschedule.prev.next = nschedule;
							nschedule.prev = cschedule.prev;
							nschedule.next = cschedule;
							cschedule.prev = nschedule;
							break;
						}
						else if ( duration <= ( cschedule.next.start - cschedule.finish - 1 ) && ttask[ cjob ] < ( cschedule.next.start - duration ) )
						{
							if ( ttask[ cjob ] > cschedule.finish )
							{
								nschedule.start = ttask[ cjob ] + 1;
								nschedule.finish = ttask[ cjob ] = ttask[ cjob ] + duration;
							}
							else
							{
								nschedule.start = cschedule.finish + 1;
								nschedule.finish = ttask[ cjob ] = cschedule.finish + duration;
							}

							cschedule.next.prev = nschedule;
							nschedule.next = cschedule.next;
							nschedule.prev = cschedule;
							cschedule.next = nschedule;
							break;
						}
					}
					cschedule = cschedule.next;
				}
				while ( cschedule );
			}
		}

		if ( makespan < tmachine [ cmachine ] )
			makespan = tmachine [ cmachine ];

		ntask [ cjob ] ++;
		nmachine [ cmachine ] ++;
	}

	return makespan;
}

int fitness ( int antiBody[], int print )
{
	int i, j, k, l, lt;
	int makespan = 0;
	int cmachine, cjob, ctask, duration;
	stime cschedule = null, nschedule = null;
	
	int cnt = 0;

	for ( i = 0 ; i < m ; i ++ )
	{
		jobs[ i ] = null;
		ntask [ i ] = ttask [ i ] = 0;
	}

	for ( i = 0 ; i < n ; i ++ )
	{
		schedule[ i ] = base[ i ] = null;
		tmachine [ i ] = nmachine [ i ] = 0;
	}

	for ( i = 0 ; i < size ; i ++ )
	{
		cjob = antiBody [ i ];
		ctask = ntask [ cjob ];
		cmachine = job[ cjob ][ ctask ][ 0 ];
		duration = job[ cjob ][ ctask ][ 1 ];

		if ( schedule[ cmachine ] == null )
		{
			nschedule = base[ cmachine ] = schedule[ cmachine ] = operation[ cnt ++ ];

			nschedule.start = ttask[ cjob ] + 1;
			nschedule.finish = ttask[ cjob ] = tmachine[ cmachine ] = ttask[ cjob ]  + duration;
			nschedule.duration = job [ cjob ][ ctask ][ 1 ];
			nschedule.job = cjob;
			nschedule.operation = ctask;
			nschedule.machine = cmachine;
			nschedule.next = nschedule.prev = null;
			
			if ( jobs[ cjob ] == null )
			{
				nschedule.lnext = nschedule.lprev = null;
				jobs[ cjob ] = nschedule;
			}
			else
			{
				cschedule = jobs[ cjob ];
				while ( cschedule.lnext != null )
					cschedule = cschedule.lnext;

				nschedule.lprev = cschedule;
				nschedule.lnext = null;
				cschedule.lnext = nschedule;
			}
		}
		else
		{
			nschedule = operation[ cnt ++ ];
			
			if ( ttask[ cjob ] >= schedule[ cmachine ].finish )
			{
				nschedule.start = ttask[ cjob ] + 1;
				nschedule.finish = tmachine[ cmachine ] = ttask[ cjob ] = ttask[ cjob ] + duration;
				nschedule.duration = duration;
				nschedule.job = cjob;
				nschedule.operation = ctask;
				nschedule.machine = cmachine;
				schedule[ cmachine ].next = nschedule;
				nschedule.prev = schedule[ cmachine ];
				nschedule.next = null;
				schedule[ cmachine ] = nschedule;
				//OK
				if ( jobs[ cjob ] == null )
				{
					nschedule.lnext = nschedule.lprev = null;
					jobs[ cjob ] = nschedule;
				}
				else
				{
					cschedule = jobs[ cjob ];
					while ( cschedule.lnext != null)
						cschedule = cschedule.lnext;

					nschedule.lprev = cschedule;
					nschedule.lnext = null;
					cschedule.lnext = nschedule;
				}
			}
			else
			{
				cschedule = base[ cmachine ];
				do
				{
					if ( cschedule.prev == null && cschedule.start > ( ttask[ cjob ] + duration ) )
					{
						nschedule.start = ttask[ cjob ] + 1;
						nschedule.finish = ttask[ cjob ] = ttask[ cjob ] + duration;
						nschedule.duration = duration;
						nschedule.job = cjob;
						nschedule.operation = ctask;
						nschedule.machine = cmachine;
						cschedule.prev = nschedule;
						nschedule.next = cschedule;
						nschedule.prev = null;
						base[ cmachine ] = nschedule;
						//OK
						if ( jobs[ cjob ] == null )
						{
							nschedule.lnext = nschedule.lprev = null;
							jobs[ cjob ] = nschedule;
						}
						else
						{
							cschedule = jobs[ cjob ];
							while ( cschedule.lnext != null )
								cschedule = cschedule.lnext;

							nschedule.lprev = cschedule;
							nschedule.lnext = null;
							cschedule.lnext = nschedule;
						}
						break;
					}
					else if ( cschedule.next == null)
					{
						nschedule.job = cjob;

						if ( cschedule.prev != null )
						{
							if ( duration <= ( cschedule.start - cschedule.prev.finish - 1 ) && ttask[ cjob ] < ( cschedule.start - duration ) )
							{
								if ( ttask[ cjob ] > cschedule.prev.finish )
								{
									nschedule.start = ttask[ cjob ] + 1;
									nschedule.finish = ttask[ cjob ] = ttask[ cjob ] + duration;
								}
								else
								{
									nschedule.start = cschedule.prev.finish + 1;
									nschedule.finish = ttask[ cjob ] = cschedule.prev.finish + duration;
								}

								nschedule.duration = duration;
								nschedule.operation = ctask;
								nschedule.machine = cmachine;
								cschedule.prev.next = nschedule;
								nschedule.prev = cschedule.prev;
								nschedule.next = cschedule;
								cschedule.prev = nschedule;		
								//OK
								if ( jobs[ cjob ] == null )
								{
									nschedule.lnext = nschedule.lprev = null;
									jobs[ cjob ] = nschedule;
								}
								else
								{
									cschedule = jobs[ cjob ];
									while ( cschedule.lnext != null )
										cschedule = cschedule.lnext;

									nschedule.lprev = cschedule;
									nschedule.lnext = null;
									cschedule.lnext = nschedule;
								}
								break;
							}
							else
							{
								if ( ttask[ cjob ] > cschedule.finish )
								{
									nschedule.start = ttask[ cjob ] + 1;
									nschedule.finish = ttask[ cjob ] = tmachine[ cmachine ] = ttask[ cjob ] + duration;
								}
								else
								{
									nschedule.start = cschedule.finish + 1;
									nschedule.finish = ttask[ cjob ] = tmachine[ cmachine ] = cschedule.finish + duration;
								}

								nschedule.duration = duration;
								nschedule.operation = ctask;
								nschedule.machine = cmachine;
								cschedule.next = nschedule;
								nschedule.prev = cschedule;
								nschedule.next = null;
								schedule[ cmachine ] = nschedule;		
								//OK
								if ( jobs[ cjob ] == null )
								{
									nschedule.lnext = nschedule.lprev = null;
									jobs[ cjob ] = nschedule;
								}
								else
								{
									cschedule = jobs[ cjob ];
									while ( cschedule.lnext != null )
										cschedule = cschedule.lnext;

									nschedule.lprev = cschedule;
									nschedule.lnext = null;
									cschedule.lnext = nschedule;
								}
								break;
							}
						}
						else
						{
							if ( ttask[ cjob ] > tmachine [ cmachine ] )
							{
								nschedule.start = ttask[ cjob ] + 1;
								nschedule.finish = tmachine[ cmachine ] = ttask[ cjob ] = ttask[ cjob ] + duration;
							}
							else
							{	
								nschedule.start = tmachine[ cmachine ] + 1;
								nschedule.finish = tmachine[ cmachine ] = ttask[ cjob ] = tmachine[ cmachine ] + duration;
							}

							nschedule.duration = duration;
							nschedule.operation = ctask;
							nschedule.machine = cmachine;
							cschedule.next = nschedule;
							nschedule.next = nschedule.lnext = nschedule.lprev = null;
							nschedule.prev = cschedule;
							schedule[ cmachine ] = nschedule;

							if ( jobs[ cjob ] == null )
							{
								nschedule.lnext = nschedule.lprev = null;
								jobs[ cjob ] = nschedule;
							}
							else
							{
								cschedule = jobs[ cjob ];
								while ( cschedule.lnext != null )
									cschedule = cschedule.lnext;

								nschedule.lprev = cschedule;
								nschedule.lnext = null;
								cschedule.lnext = nschedule;
							}

							break;
						}
					}
					else if ( cschedule.next != null && cschedule.prev != null )
					{
						if ( duration <= ( cschedule.start - cschedule.prev.finish - 1 ) && ttask[ cjob ] < ( cschedule.start - duration ) )
						{
							nschedule.job = cjob;

							if ( ttask[ cjob ] > cschedule.prev.finish )
							{
								nschedule.start = ttask[ cjob ] + 1;
								nschedule.finish = ttask[ cjob ] = ttask[ cjob ] + duration;
							}
							else
							{
								nschedule.start = cschedule.prev.finish + 1;
								nschedule.finish = ttask[ cjob ] = cschedule.prev.finish + duration;
							}

							nschedule.duration = duration;
							nschedule.job = cjob;
							nschedule.operation = ctask;
							nschedule.machine = cmachine;
							cschedule.prev.next = nschedule;
							nschedule.next = cschedule;
							nschedule.prev = cschedule.prev;
							cschedule.prev = nschedule;
							//OK
							if ( jobs[ cjob ] == null )
							{
								nschedule.lnext = nschedule.lprev = null;
								jobs[ cjob ] = nschedule;
							}
							else
							{
								cschedule = jobs[ cjob ];
								while ( cschedule.lnext != null )
									cschedule = cschedule.lnext;

								nschedule.lprev = cschedule;
								nschedule.lnext = null;
								cschedule.lnext = nschedule;
							}
							break;
						}
					}
					cschedule = cschedule.next;
				}
				while ( cschedule != null );
			}
		}

		if ( makespan < tmachine [ cmachine ] )
			makespan = tmachine [ cmachine ];

		ntask [ cjob ] ++;
		nmachine [ cmachine ] ++;
	}

	for ( i = j = 0; i < n; i++ )
	{
		cschedule = base[ i ];
		
		while (cschedule != null)
		{
			if ( cschedule.next == null )
			{
				cschedule.lstart = makespan - cschedule.duration;
				cschedule.lfinish = makespan;
				cschedule.lfloat = makespan - cschedule.finish;
			}
			else
			{
				cschedule.lstart = cschedule.next.start - cschedule.duration;
				cschedule.lfinish = cschedule.lstart + cschedule.duration - 1;
				cschedule.lfloat = cschedule.lstart - cschedule.start;
			}

			np[ j ].begin = cschedule.start;
			np[ j++ ].job = cschedule.job;
			cschedule = cschedule.next;
		}
	}
	
	k = 0;
	do
		k = 3 * k + 1;
	while ( k < size );
    
	do
	{
		k /= 3;
		for (i = k; i < size; i++)
		{
			l = np[i].begin;
			lt = np[i].job;
			j = i;
			while (np[j - k].begin > l)
			{
				np[j].begin = np[j - k].begin;
				np[j].job = np[j - k].job;
				j -= k;
				if (j < k)
					break;
			}
			np[j].begin = l;
			np[j].job = lt;
		}
	}
	while (k != 0);

	for (i = 0; i < size; i++)
		antiBody[ i ] = np[i].job;

	if ( print )
	{
		printf ( "%d %d\n", m, n );
		for ( i = 0; i < size ; i ++ )
			printf ( "%d ", antiBody [ i ] );

		printf ("\n%d\n", makespan);

		if ( print > 1 )
			for ( i = 0; i < n; i ++ )
			{
				cschedule = base[ i ];

				while (cschedule != null)
				{
					printf ( "%d %d %d\n", cschedule.job, cschedule.start, cschedule.finish );
					cschedule = cschedule.next;
				}
			}

		printf ( "#\n" );
	}
	
	//************************************************************************************************

	antiBody[ size ] = makespan;
	return makespan;
}

void mutationA ( int antiBody[])
{
	double pm = ( 1.0 / (double)size );
	mutationA(antiBody, pm);
}
void mutationA ( int antiBody[], double pm )
{
	int c , cj, nj, pj, i, j, k, machine, r = 0, t;
	int p[] = new int[ m ];
	
	do
	{
		for ( i = 0; i < m; i++ )
			p[ i ] = 0;
		for ( i = 0; i < size; i ++ )
		{
			if ( rnd.nextFlip ( pm ) )
			{
				r++;
				machine = job[ antiBody[ i ] ][ p[ antiBody[ i ] ] ][ 0 ];

				do
					j = rnd.nextInt( m );
				while ( ma[ machine ][ j ].position == ma[ machine ][ antiBody[ i ] ].position );

				cj = 0;
				nj = ma[ machine ][ j ].job;
				pj = ma[ machine ][ j ].position;

				for ( k = 0; k < size; k++ )
					if ( antiBody[ k ] == nj )
					{
						if ( pj == cj )
							break;
						else
							cj++;
					}

				c = 0;
				t = antiBody[ i ];
				
				if ( k > i )
				{
					for ( j = i; j + c < k; j++ )
					{
						if ( antiBody[ j + 1 + c ] == t )
							c++;

						antiBody[ j ] = antiBody[ j + c + 1 ]; 
					}

					for ( ; j <= k; j++ )
						antiBody[ j ] = t;
				}
				else if ( k < i )
				{
					for ( j = i; j - c > k; j-- )
					{
						if ( antiBody[ j - 1 - c ] == t )
							c++;

						antiBody[ j ] = antiBody[ j - c - 1 ]; 
					}

					for ( ; j >= k; j-- )
						antiBody[ j ] = t;
				}
			
				for ( j = 0; j < m; j++ )
					p[ j ] = 0;

				for ( j = 0; j < i; j++ )
					p[ antiBody[ j ] ]++;
			}
			p[ antiBody[ i ] ]++;
		}
	}
	while ( r == 0 );
	delete p;
}


void mutationB ( int antiBody[] )
{
	double pm = ( 1.0 / (double) size  );
	mutationB(antiBody, pm);
}
void mutationB ( int antiBody[], double pm )
{
	int cj, nj, pj, i, j, k, machine, r = 0, t;
	int p[] = new int[ m ];

	do
	{
		for ( i = 0; i < m; i++ )
			p[ i ] = 0;

		for ( i = 0; i < size; i ++ )
		{
			if ( rnd.nextFlip ( pm ) )
			{
				r++;
				machine = job[ antiBody[ i ] ][ p[ antiBody[ i ] ] ][ 0 ];

				do 
					j = rnd.nextInt( m );
				while ( ma[ machine ][ j ].position == ma[ machine ][ antiBody[ i ] ].position );

				cj = 0;
				nj = ma[ machine ][ j ].job;
				pj = ma[ machine ][ j ].position;

				for ( k = 0; k < size; k++ )
					if ( antiBody[ k ] == nj )
					{
						if ( pj == cj )
							break;
						else
							cj++;
					}

				t = antiBody[ i ];
				antiBody[ i ] = antiBody[ k ];
				antiBody[ k ] = t;

			
				for ( j = 0; j < m; j++ )
					p[ j ] = 0;

				for ( j = 0; j < i; j++ )
					p[ antiBody[ j ] ]++;
			}
			p[ antiBody[ i ] ]++;
		}
	}
	while ( r == 0 );
	delete p;
}

void mutationC ( int antiBody[])
{
	double pm = 0.005;
	mutationC(antiBody, pm);
}
void mutationC ( int antiBody[], double pm)
{
	int i, j, r, t;
	int size = m * n;

	for ( i = size/2; i < size; i ++ )
		if ( rnd.nextFlip ( pm ) )
		{
			do
				j = rnd.nextInt( size );
			while ( i == j );

			if ( i < j )
			{
				t = antiBody[ i ];

				for ( r = i; r < j; r++)
					antiBody[ r ] = antiBody[ r + 1];

				antiBody[ r ] = t; 
			}
			else
			{
				t = antiBody[ i ];

				for ( r = i; r > j; r-- )
					antiBody[ r ] = antiBody[ r - 1];

				antiBody[ r ] = t;
			}
		}

		for ( i = 0; i < size/2; i ++ )
			if ( rnd.nextFlip ( pm ) )
			{
				do
					j = rnd.nextInt( size );
				while ( antiBody[ i ] == antiBody[ j ] );

				t = antiBody[ j ];
				antiBody[ j ] = antiBody[ i ];
				antiBody[ i ] = t;
			}
}


void mutationD ( int w[], double pm )
{
	int i, co, so, s, t, inc;
	stime op[], sw[];
	pm = 0;

	int antiBody[] = new int [ size + 1 ];
	//Una operaci�n de la ruta cr�tica

	do 
	{
	
	copy (w, antiBody );

	do
		op = operation[ rnd.nextInt( 0, size ) ];
	while ( op.lfloat != 0 );

	if ( op.prev == null )
		op = op.next;

	//La operaci�n anterior a la previamente seleccionada
	sw = op.prev;

	//Encontramos la posici�n de la operaci�n en la cadena
	for ( s = co = 0; s < size; s++ )
		if ( op.job == antiBody[ s ] )
		{
			if ( co == op.operation )
				break;
			else
				co++;
		}
	//Encontramos la posici�n de la operaci�n anterior en la cadena
	for ( t = so = 0; t < size; t++ )
		if ( sw.job == antiBody[ t ] )
		{
			if ( so == sw.operation )
				break;
			else
				so++;
		}

	if ( s > t )
	{
		for ( i = t, inc = 0; i + inc <= s; i++ )
		{
			while ( antiBody[ i + inc ] == sw.job )
				inc ++;

			antiBody[ i ] = antiBody[ i + inc ]; 
		}

		for ( ; i <= s; i++ )
			antiBody[ i ] = sw.job;
	}
	else if ( s < t )
	{
		for ( i = t, inc = 0; i -inc >= s; i-- )
		{
			while ( antiBody[ i - inc ] == sw.job )
				inc ++;

			antiBody[ i ] = antiBody[ i - inc ]; 
		}

		for ( ; i >= s; i-- )
			antiBody[ i ] = sw.job;
	}


	}
	while ( w[ size ] < evaluation( antiBody ) );

	copy ( antiBody, w );
	delete antiBody;
}

void mutationE ( int *antiBody, double pm )
{
	int i, co, so, s, t, inc;
	struct stime *op, *sw;
	pm = 0;

	//Una operaci�n de la ruta cr�tica

	for ( register int x = 0; x < 2; x++ ){

	do
	{
		do
		{
			op = &operation[ rnd.nextInt( 0, size ) ];
		}
		while ( op.lnext == null );
	}
	while ( ( op.lnext.start - op.finish )  == 1 );

	//La operaci�n anterior a la previamente seleccionada
	sw = op.lnext;

	//Encontramos la posici�n de la operaci�n en la cadena
	for ( s = co = 0; s < size; s++ )
		if ( op.job == antiBody[ s ] )
		{
			if ( co == op.operation )
				break;
			else
				co++;
		}
	//Encontramos la posici�n de la operaci�n anterior en la cadena
	for ( t = so = 0; t < size; t++ )
		if ( sw.job == antiBody[ t ] )
		{
			if ( so == sw.operation )
				break;
			else
				so++;
		}

	if ( s > t )
		return;

	for ( i = t, inc = 0; i - 1 > s; i-- )
		antiBody[ i ] = antiBody[ i - 1 ]; 

	for ( ; i > s; i-- )
		antiBody[ i ] = sw.job;

	}

}

int *clone ( int *antiBody )
{
	int *nantyBody = new int[ size + 1 ];

	for ( register int i = 0; i <= size; i ++ )
		nantyBody[ i ] = antiBody[ i ];

	return nantyBody;
}

void copy ( int a[], int b[] )
{
	for ( int i = 0; i <= size; i ++ )
		b[ i ] = a[ i ];
}


int[] gAntigen()
{
	int ag[] = new int [ size + 1 ];
	int i, j;

	for ( i = 0; i < m ; i ++ )
		for ( j = 0; j < n ; j ++ )
			ag[ i * n + j] = i;

	for ( i = 0; i < size ; i ++ ){
		int index = rnd.nextInt( i, size );
		int tmp = ag[ i ];
		ag[ i ] = ag[ index ];
		ag[ index ] = tmp;
	}

	ag[ size ] = 0;

	return ag;
}

int main ( int argc, String argv[] )
{
	int ag[] = null, ab[] = null, worst[] = null, bb[] = null;
	int fag = 0, fab = 0, fw = 0, fbb = 0, m = 0, p = 0;
	double fprom = 0.0, pm = 0.0, fp = 0.0, sd = 0.0, va = 0.0;
	long i = 0, j = 0, k = 0;
	

	if ( argc < 6 || argc > 8 )
	{
		printf ( "use: \n" );
		printf ( "jssp { s | n } {$ filename} {# antigens} {# population} {#.# pm} [# rand seed] [$ filename]\n\n" );
		printf ( "ejemplo:" );
		printf ( "jssp s ft06.txt 4 20480 0.04 \n\n" );
		exit ( - 1 );
	}

	//int ( *objective)( int*, int ) = ( *argv[1] == 's' ) ? fitness : fitnesswols;
	int ( *objective)( int*, int ) = fitness;

	loadjssp ( argv[ 2 ] );
	nag = atoi ( argv[ 3 ] );
	population = atoi ( argv[ 4 ] );
	pm = atof ( argv[ 5 ] );

	switch ( argc )
	{
		case 6:
			rnd = new Random ( );	
			break;
		case 7:
			if ( argv[6][0] >= '0' && argv[6][0] <= '9' )
				rnd = new Random ( _atoi64 ( argv[ 6 ] ) ); 
			else
			{
				ag = loadsolution ( argv[ 6 ] );
				rnd = new Random ( );
			}
			break;
		case 8:	
			if ( argv[6][0] >= '0' && argv[6][0] <= '9' )
			{
				ag = loadsolution ( argv[ 7 ] );
				rnd = new Random ( _atoi64 ( argv[ 6 ] ) ); 
			}
			else
			{
				ag = loadsolution ( argv[ 6 ] );
				rnd = new Random ( _atoi64 ( argv[ 7 ] ) ); 
			}
			break;
	}


	int *r = new int[ nag ];
	long *ng = new long[ nag ];
	worst = new int [ size ];
	
	for ( k = 0; k < nag; k ++ )
	{
		printf ( "Seed: %I64i\n", rnd.getInitSeed ( ) );
		printf ( "********************\n%d\n\n", k );

		if ( ag == null )
			ag = gAntigen ( );
		
		ab = gAntigen ( );
		fw = fab = fbb = objective ( ab, 0 );
		fprom = 0.0;

		for ( i = 0; i < population; i ++ )
		{
			fprom += fag = objective ( ag, 0 );
			if ( fag > fw )
				copy ( ag, worst );

			if ( fag < ( fab + 2 ) )
			{
				fab = fag;
				delete ab;
				ab = clone ( ag );

				if ( fab < fbb )
				{
					fbb = fag;
					ng[ k ] = i;
					
					if ( bb != null )
						delete bb;

					bb = clone ( ag );
					printf ( "%d,%d\n", i, fab );
					objective( ag, 2 );
				}
			}
			else //if ( rnd.nextFlip ( 0.5 ) )
				for ( j = 0; j < size; j ++ )
					ag[ j ] = ab [ j ];

			if ( rnd.nextFlip( 0.5 ) )
				mutationA ( ag, pm );
			else if ( rnd.nextFlip( 0.33 ) )
				mutationB( ag, pm );
			else if ( rnd.nextFlip( 0.5 ) )
				mutationD( ag, pm );
			else
				mutationE( ag, pm );

			//mutationD( ag, pm );
		}

		printf ( "\nPromedio: %0.5lf\n\nPeor:\n", fprom / population );
		if ( worst != null )
			objective ( worst, 1 );
		printf ( "Mejor:\n" );

		r[ k ] = bb != null ? objective ( bb, 2 ) : 0;
		printf ( "\n\n%d\n********************\n", k );
		delete ab;
		delete ag;

		ab = ag = null;
	}

	printf( "Estadisticas:\n\n" );

	//Calculo de la media de las mejores soluciones
	for ( i = 0; i < nag; i++ )
	{
		fp += r[ i ];

		if ( r[ m ] > r[ i ] )
			m = i;

		if ( r[ p ] < r[ i ] )
			p = i;
	}
	fp /= double(nag);

	//Calculo de la varianza
	for ( i = 0; i < nag; i++ )
		va += double( r[ i ] ) * double( r[ i ] );

	va /= double( nag );
	
	va -= fp * fp;
	//Calculo de la desviaci�n est�ndar
	sd = sqrt ( va );

	puts ( "Soluciones\n" );
	for ( i = 0; i < nag; i++ )
		printf ( "%d %ld %d %s\n", i, ng[ i ],  r[ i ], ( i == m ) ? "M" : (( i == p ? "P" : "" )) );

	puts ( "\n" );
	printf ( "Promedio: %f\n", fp );
	printf ( "Varianza: %f\n", va );
	printf ( "DesvTipi: %f\n", sd );
	
	while (getch() != 's' );

	return 0;
}



static void printf (String s){
	
}
static void printf (String s,double d){
	
}
}