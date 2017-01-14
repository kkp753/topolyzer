
from math import floor as fl
from math import sqrt

from hc_ll import ll as hc_ll

# LL_HC(1) = 1
# LL_HC(2) = 4
# LL_HC(3) = 16
# LL_HC(n) = 2 * LL_HC(n-1) + ...

# k=3:
#  mmmmmmmmmmmmmmmmkkk
#  \______/\______/\_/
#   2^(2^k)-bit*m  +  k-bit

# LL_MC(1,0) = LL_HC(1) = 1
# LL_MC(1,1) = 4*LL_MC(1,0) + (1 + 2)*1*2                   .. 3
# LL_MC(1,2) = 4*LL_MC(1,1) + (2 + 4)*2*4                   .. 6
# LL_MC(1,3) = 4*LL_MC(1,2) + (4 + 8)*4*8                   .. 12
# LL_MC(1,4) = 4*LL_MC(1,3) + (8 + 16)*8*16                 .. 24
#
# LL_MC(2,0) = LL_HC(2) = 4
# LL_MC(2,1) = 16*LL_MC(2,0) + (2+4 + 2+4)*2*4              ..  4  +   8
# LL_MC(2,2) = 16*LL_MC(2,1) + (8+16 + 8+16)*8*16           .. 16  +  32
# LL_MC(2,3) = 16*LL_MC(2,2) + (32+64 + 32+64)*32*64        .. 64  + 128
#
# LL_MC(3,0) = LL_HC(3) = 16
# LL_MC(3,1) = 256*LL_MC(3,0) + (2+4+8+16 + 4+8+16+32)*8*16 .. 6 + 12 + 24 + 48
#                                                      128*256
# LL_MC(4,0) = LL_HC(4) = 48

# LL_MC(k,m) = 2^(2^k)*LL_MC(k,m-1) + (vl_sum+hl_sum) *  


def ll(k, m):
	if (m == 0):
		return hc_ll(k)
	else:
		# num of clusters ( cluster: MC(k,m-1) )
		#  (1,m):2x2->4, (2,m):4x4->15, (3,m):16x16->256, (4,m):256x256=65536
		nc = 2**(2**k)
		# ary: 2x2,4x4,16x16,256x256,...
		kc = int(sqrt(nc))
		# size of cluster (row+col)
		#  (1,0):1x2->3,  (2,0):2x2->4,    (3,0):2x4->6,    (4,0):4x4->8, (5,0):4x8->12
		#  (1,1):2x4->6,  (2,1):8x8->16,   (3,1):32x64->96
		#  (1,2):4x8->12, (2,2):32x32->64
		if k==1:
			sc = (1+2) * kc**(m-1)
		elif k==2:
			sc = (2+2) * kc**(m-1)
		elif k==3:
			sc = (2+4) * kc**(m-1)
		else:
			return 0
		# total length of additional links (for row/col)
		#  (1,m):sc,  (2,m):sc+2*sc, (3,m):sc+2*sc+4*sc+8*sc
		#        1sc       3sc             15sc
		tl = sc*(kc-1)
		# num of additional links
		#  (1,0):1x2, (2,0):2x4,  (3,0):8x16
		#  (1,1):2x4, (2,1):8x16, (3,1):128x256
		nl = int(kc**m * kc**m / 2)

		# total
		return nc*ll(m,k-1) + tl*nl


def N(k,m):
	return 2**(k + m*2**k)

def d(k,m):
	return m+k

def D(k,m):
	return 2**k * (m+1)


print("#    N  d  D          L")

for k in range(1,4):
	for m in range(0,int(10/k)):
		fmt = "{N:6d} {d:2d} {D:2d} {L:10d} # MC({k},{m})"
		print(fmt.format(N
		=N(k,m), d=d(k,m), D=D(k,m), L=ll(k,m), k=k, m=m))
	print("")
	


