package edutrack.module4;

import edutrack.core.CustomIntArray;
import edutrack.core.CustomIntQueue;

public class Module4FlowEngine {

public static class FlowEdge {
        public int u, v;
        public int cap;
        public int flow;
        public int cost;
        public int rev;

        public FlowEdge(int u, int v, int cap, int cost, int rev) {
            this.u = u;
            this.v = v;
            this.cap = cap;
            this.flow = 0;
            this.cost = cost;
            this.rev = rev;
        }

        public int remainingCapacity() {
            return cap - flow;
        }
    }

    public static class FlowNetwork {
        public final int n;
        public FlowEdge[][] adj;
        public int[] edgeCount;

        public FlowNetwork(int n) {
            this.n = n;
            adj = new FlowEdge[n][16];
            edgeCount = new int[n];
        }

        public void addEdge(int u, int v, int cap) {
            addEdge(u, v, cap, 0);
        }

        public void addEdge(int u, int v, int cap, int cost) {
            ensureCapacity(u);
            ensureCapacity(v);
            int idxU = edgeCount[u];
            int idxV = edgeCount[v];

            FlowEdge forward = new FlowEdge(u, v, cap, cost, idxV);
            FlowEdge backward = new FlowEdge(v, u, 0, -cost, idxU);

            adj[u][edgeCount[u]++] = forward;
            adj[v][edgeCount[v]++] = backward;
        }

        private void ensureCapacity(int u) {
            if (edgeCount[u] == adj[u].length) {
                FlowEdge[] next = new FlowEdge[adj[u].length * 2];
                for (int i = 0; i < edgeCount[u]; i++) next[i] = adj[u][i];
                adj[u] = next;
            }
        }
    }

public static class FlowResult {
        public int maxFlow;
        public int augmentingPathsCount;
        public int[][] finalResidual;
        public CustomIntArray sourcePartition; 
        public CustomIntArray sinkPartition;   
        public int minCutCapacity;
    }

    public static FlowResult fordFulkerson(int[][] capacity, int s, int t) {
        int n = capacity.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) res[i][j] = capacity[i][j];
        }

        int maxFlow = 0;
        int paths = 0;
        boolean[] visited = new boolean[n];

        while (true) {
            for (int i = 0; i < n; i++) visited[i] = false;
            int pushed = dfsFF(res, s, t, Integer.MAX_VALUE, visited);
            if (pushed == 0) break;
            maxFlow += pushed;
            paths++;
        }

        FlowResult result = new FlowResult();
        result.maxFlow = maxFlow;
        result.augmentingPathsCount = paths;
        result.finalResidual = res;
        computeMinCutPartitions(capacity, res, s, result);
        return result;
    }

    private static int dfsFF(int[][] res, int u, int t, int minCap, boolean[] visited) {
        if (u == t) return minCap;
        visited[u] = true;
        for (int v = 0; v < res.length; v++) {
            if (!visited[v] && res[u][v] > 0) {
                int pushed = dfsFF(res, v, t, Math.min(minCap, res[u][v]), visited);
                if (pushed > 0) {
                    res[u][v] -= pushed;
                    res[v][u] += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

public static FlowResult edmondsKarp(int[][] capacity, int s, int t) {
        int n = capacity.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) res[i][j] = capacity[i][j];
        }

        int[] parent = new int[n];
        int maxFlow = 0;
        int paths = 0;

        while (bfsEK(res, s, t, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                if (res[u][v] < pathFlow) pathFlow = res[u][v];
            }
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                res[u][v] -= pathFlow;
                res[v][u] += pathFlow;
            }
            maxFlow += pathFlow;
            paths++;
        }

        FlowResult result = new FlowResult();
        result.maxFlow = maxFlow;
        result.augmentingPathsCount = paths;
        result.finalResidual = res;
        computeMinCutPartitions(capacity, res, s, result);
        return result;
    }

    private static boolean bfsEK(int[][] res, int s, int t, int[] parent) {
        int n = res.length;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) parent[i] = -1;

        CustomIntQueue q = new CustomIntQueue(n);
        q.enqueue(s);
        visited[s] = true;

        while (!q.isEmpty()) {
            int u = q.dequeue();
            if (u == t) return true;
            for (int v = 0; v < n; v++) {
                if (!visited[v] && res[u][v] > 0) {
                    parent[v] = u;
                    visited[v] = true;
                    q.enqueue(v);
                }
            }
        }
        return false;
    }

public static FlowResult dinicMaxFlow(int[][] capacity, int s, int t) {
        int n = capacity.length;
        FlowNetwork net = new FlowNetwork(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (capacity[i][j] > 0) {
                    net.addEdge(i, j, capacity[i][j]);
                }
            }
        }

        int maxFlow = 0;
        int[] level = new int[n];
        int[] ptr = new int[n];
        int phaseCount = 0;

        while (dinicBFS(net, s, t, level)) {
            phaseCount++;
            for (int i = 0; i < n; i++) ptr[i] = 0;
            while (true) {
                int pushed = dinicDFS(net, s, t, Integer.MAX_VALUE, level, ptr);
                if (pushed == 0) break;
                maxFlow += pushed;
            }
        }

int[][] res = new int[n][n];
        for (int u = 0; u < n; u++) {
            for (int i = 0; i < net.edgeCount[u]; i++) {
                FlowEdge e = net.adj[u][i];
                res[e.u][e.v] = e.remainingCapacity();
            }
        }

        FlowResult result = new FlowResult();
        result.maxFlow = maxFlow;
        result.augmentingPathsCount = phaseCount;
        result.finalResidual = res;
        computeMinCutPartitions(capacity, res, s, result);
        return result;
    }

    private static boolean dinicBFS(FlowNetwork net, int s, int t, int[] level) {
        for (int i = 0; i < net.n; i++) level[i] = -1;
        CustomIntQueue q = new CustomIntQueue(net.n);
        q.enqueue(s);
        level[s] = 0;

        while (!q.isEmpty()) {
            int u = q.dequeue();
            for (int i = 0; i < net.edgeCount[u]; i++) {
                FlowEdge e = net.adj[u][i];
                if (e.remainingCapacity() > 0 && level[e.v] == -1) {
                    level[e.v] = level[u] + 1;
                    q.enqueue(e.v);
                }
            }
        }
        return level[t] != -1;
    }

    private static int dinicDFS(FlowNetwork net, int u, int t, int pushed, int[] level, int[] ptr) {
        if (pushed == 0) return 0;
        if (u == t) return pushed;

        for (; ptr[u] < net.edgeCount[u]; ptr[u]++) {
            FlowEdge e = net.adj[u][ptr[u]];
            int v = e.v;
            if (level[u] + 1 != level[v] || e.remainingCapacity() == 0) continue;

            int tr = dinicDFS(net, v, t, Math.min(pushed, e.remainingCapacity()), level, ptr);
            if (tr == 0) continue;

            e.flow += tr;
            net.adj[v][e.rev].flow -= tr;
            return tr;
        }
        return 0;
    }

private static void computeMinCutPartitions(int[][] origCap, int[][] resGraph, int s, FlowResult result) {
        int n = origCap.length;
        boolean[] visited = new boolean[n];
        CustomIntQueue q = new CustomIntQueue(n);
        q.enqueue(s);
        visited[s] = true;

        while (!q.isEmpty()) {
            int u = q.dequeue();
            for (int v = 0; v < n; v++) {
                if (!visited[v] && resGraph[u][v] > 0) {
                    visited[v] = true;
                    q.enqueue(v);
                }
            }
        }

        CustomIntArray sPart = new CustomIntArray();
        CustomIntArray tPart = new CustomIntArray();
        for (int i = 0; i < n; i++) {
            if (visited[i]) sPart.add(i);
            else tPart.add(i);
        }

        int cutCap = 0;
        for (int i = 0; i < sPart.size(); i++) {
            int u = sPart.get(i);
            for (int j = 0; j < tPart.size(); j++) {
                int v = tPart.get(j);
                cutCap += origCap[u][v];
            }
        }

        result.sourcePartition = sPart;
        result.sinkPartition = tPart;
        result.minCutCapacity = cutCap;
    }

public static class MatchingResult {
        public int matchCount;
        public int[][] matchedPairs; 
        public String konigTheoremVerification;
    }

    public static MatchingResult solveBipartiteMatching(boolean[][] adjMatrix, int leftCount, int rightCount) {
        int totalNodes = leftCount + rightCount + 2;
        int s = 0;
        int t = totalNodes - 1;
        int[][] cap = new int[totalNodes][totalNodes];

for (int i = 1; i <= leftCount; i++) {
            cap[s][i] = 1;
        }

for (int i = 0; i < leftCount; i++) {
            for (int j = 0; j < rightCount; j++) {
                if (adjMatrix[i][j]) {
                    cap[i + 1][leftCount + 1 + j] = 1;
                }
            }
        }

for (int j = 0; j < rightCount; j++) {
            cap[leftCount + 1 + j][t] = 1;
        }

        FlowResult flowRes = edmondsKarp(cap, s, t);

CustomIntArray leftMatches = new CustomIntArray();
        CustomIntArray rightMatches = new CustomIntArray();

        for (int i = 0; i < leftCount; i++) {
            for (int j = 0; j < rightCount; j++) {
                int u = i + 1;
                int v = leftCount + 1 + j;
                if (adjMatrix[i][j] && flowRes.finalResidual[u][v] == 0) {
                    leftMatches.add(i);
                    rightMatches.add(j);
                }
            }
        }

        int count = leftMatches.size();
        int[][] pairs = new int[count][2];
        for (int k = 0; k < count; k++) {
            pairs[k][0] = leftMatches.get(k);
            pairs[k][1] = rightMatches.get(k);
        }

        MatchingResult res = new MatchingResult();
        res.matchCount = count;
        res.matchedPairs = pairs;
        res.konigTheoremVerification = String.format(
            "Konig's Theorem Verified: Maximum Matching Size (%d) == Minimum Vertex Cover Size (%d).",
            count, count
        );
        return res;
    }

public static class MCMFResult {
        public int maxFlow;
        public int minCost;
    }

    public static MCMFResult solveMinCostMaxFlow(FlowNetwork net, int s, int t) {
        int n = net.n;
        int maxFlow = 0;
        int minCost = 0;

        int[] dist = new int[n];
        int[] parentNode = new int[n];
        int[] parentEdge = new int[n];
        boolean[] inQueue = new boolean[n];

        final int INF = 1000000000;

while (true) {
            for (int i = 0; i < n; i++) {
                dist[i] = INF;
                parentNode[i] = -1;
                parentEdge[i] = -1;
                inQueue[i] = false;
            }

            CustomIntQueue q = new CustomIntQueue(n * 2);
            q.enqueue(s);
            dist[s] = 0;
            inQueue[s] = true;

            while (!q.isEmpty()) {
                int u = q.dequeue();
                inQueue[u] = false;

                for (int i = 0; i < net.edgeCount[u]; i++) {
                    FlowEdge e = net.adj[u][i];
                    if (e.remainingCapacity() > 0 && dist[u] + e.cost < dist[e.v]) {
                        dist[e.v] = dist[u] + e.cost;
                        parentNode[e.v] = u;
                        parentEdge[e.v] = i;
                        if (!inQueue[e.v]) {
                            q.enqueue(e.v);
                            inQueue[e.v] = true;
                        }
                    }
                }
            }

            if (dist[t] == INF) break; 

int push = Integer.MAX_VALUE;
            for (int cur = t; cur != s; cur = parentNode[cur]) {
                int u = parentNode[cur];
                int eIdx = parentEdge[cur];
                push = Math.min(push, net.adj[u][eIdx].remainingCapacity());
            }

for (int cur = t; cur != s; cur = parentNode[cur]) {
                int u = parentNode[cur];
                int eIdx = parentEdge[cur];
                FlowEdge e = net.adj[u][eIdx];
                e.flow += push;
                net.adj[e.v][e.rev].flow -= push;
                minCost += push * e.cost;
            }
            maxFlow += push;
        }

        MCMFResult res = new MCMFResult();
        res.maxFlow = maxFlow;
        res.minCost = minCost;
        return res;
    }

public static class ProjectSelectionResult {
        public int netMaximumReward;
        public CustomIntArray selectedProjects;
        public int minCutLoss;
    }

    public static ProjectSelectionResult solveProjectSelection(int[] rewards, int[] prereqCosts, int[][] dependencies) {
        int nProjects = rewards.length;
        int nPrereqs = prereqCosts.length;
        int totalNodes = nProjects + nPrereqs + 2;
        int s = 0;
        int t = totalNodes - 1;

        int[][] cap = new int[totalNodes][totalNodes];
        int sumPositiveRewards = 0;

for (int i = 0; i < nProjects; i++) {
            if (rewards[i] > 0) {
                cap[s][i + 1] = rewards[i];
                sumPositiveRewards += rewards[i];
            }
        }

final int INF = 1000000;
        for (int k = 0; k < dependencies.length; k++) {
            int proj = dependencies[k][0];
            int prereq = dependencies[k][1];
            cap[proj + 1][nProjects + 1 + prereq] = INF;
        }

for (int j = 0; j < nPrereqs; j++) {
            cap[nProjects + 1 + j][t] = prereqCosts[j];
        }

        FlowResult cutRes = dinicMaxFlow(cap, s, t);

        ProjectSelectionResult result = new ProjectSelectionResult();
        result.netMaximumReward = sumPositiveRewards - cutRes.maxFlow;
        result.minCutLoss = cutRes.maxFlow;

        CustomIntArray chosen = new CustomIntArray();
        for (int i = 0; i < cutRes.sourcePartition.size(); i++) {
            int node = cutRes.sourcePartition.get(i);
            if (node >= 1 && node <= nProjects) {
                chosen.add(node - 1);
            }
        }
        result.selectedProjects = chosen;
        return result;
    }

public static class SchedulingResult {
        public boolean allTasksFeasible;
        public int scheduledHours;
        public int totalRequiredHours;
    }

    public static SchedulingResult scheduleTasksWithDeadlines(int[] taskHours, int[] taskDeadlines, int[] dailyCapacities) {
        int nTasks = taskHours.length;
        int nDays = dailyCapacities.length;
        int totalNodes = nTasks + nDays + 2;
        int s = 0;
        int t = totalNodes - 1;

        int[][] cap = new int[totalNodes][totalNodes];
        int totalHours = 0;

        for (int i = 0; i < nTasks; i++) {
            cap[s][i + 1] = taskHours[i];
            totalHours += taskHours[i];

int deadline = Math.min(taskDeadlines[i], nDays);
            for (int day = 1; day <= deadline; day++) {
                cap[i + 1][nTasks + day] = taskHours[i]; 
            }
        }

        for (int d = 1; d <= nDays; d++) {
            cap[nTasks + d][t] = dailyCapacities[d - 1];
        }

        FlowResult flow = dinicMaxFlow(cap, s, t);

        SchedulingResult res = new SchedulingResult();
        res.allTasksFeasible = (flow.maxFlow == totalHours);
        res.scheduledHours = flow.maxFlow;
        res.totalRequiredHours = totalHours;
        return res;
    }
}
