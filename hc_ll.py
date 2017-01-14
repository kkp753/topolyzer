
from math import floor as fl

def ll(n):
	if (n == 1):
		return 1
	else:
		return 2*ll(n-1) + (2**fl((n-1)/2))*(2**(n-1))


for n in range(1,20):
	#print("Total link length of HC({0:2d}) : {1:5d}".format(i, ll(i)))
	fmt = "N,d,D,L of HC({0:2d}) : {1:5d} {2:2d} {3:2d} {4:7d}"
	N = 2**n
	d = n
	D = n
	L = ll(n)

	print(fmt.format(n,N,d,D,L))


