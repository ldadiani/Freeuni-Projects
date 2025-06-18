#include <bits/stdc++.h>
using namespace std;
#define int long long 
const int S = 20005;

int N,ans;
map<vector<int> , int> mp;
vector<int> score;
string str[S];
int a[S];

void updSc(int l, int r, char ch, bool b) {
	for (int i = 1; i <= N; i ++ ) {
        if (str[i][l] == ch) {
            if(!b)score[i]--;
            else score[i]++;
        } 
    }
}

void dfs(int l, int r) {
    if (l > r) {
        if (l == 6) {
            mp[score] += 1;
        }
        if (l == 11) {
            for (int i = 1; i <= N; i ++ ) {
                score[i] = a[i] - score[i];
            }
            ans += mp[score];
            for (int i = 1; i <= N; i ++ ) {
                score[i] = a[i] - score[i];
            }
        }
        return;
    }
    updSc(l,r,'A',false);
    dfs(l + 1, r);
    updSc(l,r,'A',true);
    updSc(l,r,'B',false);
    dfs(l + 1, r);
    updSc(l,r,'B',true);
    updSc(l,r,'C',false);
    dfs(l + 1, r);
    updSc(l,r,'C',true);
    updSc(l,r,'D',false);
    dfs(l + 1, r);
    updSc(l,r,'D',true);
}
 
signed main() {
    int T;
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    
    cin >> T;
    while (T -- ) {
    	
    	mp.clear();
    	score.clear();
    	
        cin >> N;
        ans = 0;
        score.push_back(0);
        for (int i = 1; i <= N; i++ ) {
            cin >> str[i] >> a[i];
            a[i] /= 10;
            score.push_back(a[i]);
            str[i] = " " + str[i];
        } 
 
        dfs(1, 5);
        dfs(6, 10);
        cout << ans << '\n';
    }
}
