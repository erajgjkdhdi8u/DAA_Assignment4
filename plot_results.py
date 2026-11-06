import csv
from collections import defaultdict
from pathlib import Path
from statistics import median
import matplotlib.pyplot as plt

# Read the measurements made by Experiment.java.
rows = []
with open('results/results.csv', newline='', encoding='utf-8') as f:
    for row in csv.DictReader(f):
        row['n'] = int(row['n'])
        row['time_ms'] = float(row['time_ms'])
        row['operations'] = int(row['operations'])
        rows.append(row)

folder = Path('results/plots')
folder.mkdir(parents=True, exist_ok=True)


def values(algorithm, kind, field):
    grouped = defaultdict(list)
    for row in rows:
        if row['algorithm'] == algorithm and row['input'] == kind:
            grouped[row['n']].append(row[field])
    return sorted(grouped), [median(grouped[n]) for n in sorted(grouped)]


# 1. Recursive calls. The logarithmic axis makes the large difference visible.
plt.figure(figsize=(8, 4.4))
for kind in ['random', 'different']:
    x, y = values('recursive', kind, 'operations')
    plt.plot(x, y, marker='o', label=kind)
plt.yscale('log')
plt.xticks([2, 4, 6, 8])
plt.xlabel('String length n (characters)')
plt.ylabel('Recursive calls (count, log scale)')
plt.title('Recursive edit distance: number of calls')
plt.legend()
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig(folder / '01_recursive_calls.png', dpi=170)
plt.close()

# 2. Measured time on the same two difficult input strings.
plt.figure(figsize=(8, 4.4))
for method in ['table', 'two_rows']:
    x, y = values(method, 'different', 'time_ms')
    plt.plot(x, y, marker='o', label=method)
plt.xticks([100, 200, 400, 800])
plt.xlabel('String length n (characters)')
plt.ylabel('Median execution time (ms, 5 runs)')
plt.title('Edit distance: full table vs two rows')
plt.legend()
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig(folder / '02_dp_time.png', dpi=170)
plt.close()

# 3. Counts on the hard-to-find pattern. Pattern length = 128.
plt.figure(figsize=(8, 4.4))
for method in ['naive', 'kmp']:
    x, y = values(method, 'difficult', 'operations')
    plt.plot(x, y, marker='o', label=method)
plt.xticks([20000, 40000, 80000, 160000])
plt.yscale('log')
plt.xlabel('Text length n (characters)')
plt.ylabel('Character comparisons (count, log scale)')
plt.title('String search on difficult input (pattern length 128)')
plt.legend()
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig(folder / '03_search_comparisons.png', dpi=170)
plt.close()

print('Saved 3 graphs to results/plots/')
