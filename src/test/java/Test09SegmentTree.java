import java.util.Random;

public class Test09SegmentTree
{
	public static void main(String[] args)
	{
		Random random=new Random();
		int n=100,q=20;
		int a[]=new int[n+1];
		System.out.println(n);
		for(int i=0;i<q;i++)
		{
			int op=random.nextInt(2)+1;
//			System.out.println(op);
			if(op==1)
			{
				int l,r,x;
				r=random.nextInt(n)+1;
				l=random.nextInt(r)+1;
				x=random.nextInt(2*n+1)-n;
				for(int j=l;j<=r;j++)
					a[j]+=x;
				System.out.println("1 %d %d %d".formatted(l,r,x));
			}
			else
			{
				int l,r;
				r=random.nextInt(n)+1;
				l=random.nextInt(r)+1;
				int ans=0;
				for(int j=l;j<=r;j++)
					ans+=a[j];
				System.out.println("2 %d %d".formatted(l,r));
				System.out.println("ans %d".formatted(ans));
			}
		}
	}
}
