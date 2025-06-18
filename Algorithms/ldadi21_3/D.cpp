#include<bits/stdc++.h>
#define endl '\n'
using namespace std;
 
const int N = 1e6+5, mod = 998244353;
 
long long fac1[N], fac2[N], inv[N];
 
int main(){
 
    ios::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
 
    int n;
    cin >> n;
    long long A[n+1];
    for(int i = 1; i < n; i++){
        cin >> A[i];
    }
    
    fac2[0] = 1;
    for(int i = 1; i <= n; i++){
        fac2[i] = fac2[i-1]*i % mod;
    }
 	
 	inv[0] = 1;
    inv[1] = 1;
    fac1[0] = 1;
    fac1[1] = 1;
 
    for(int i = 2; i <= n; i++){
        inv[i] = mod - mod/i * inv[mod%i] % mod;
        fac1[i] = fac1[i-1] * inv[i] % mod;
    }
 
    long long ans = 1;
    for(int i = n-2; i >= 1; i--){
        assert(A[i] <= A[i+1]+1);
        ans *= fac2[A[i+1]+1]*fac1[A[i]]%mod*fac1[A[i+1]+1-A[i]]%mod;
        ans %= mod;
    }
 
    cout << ans << endl;
}
