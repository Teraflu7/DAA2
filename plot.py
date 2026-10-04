import os
import pandas as pd
import matplotlib.pyplot as plt

os.makedirs("results/plots", exist_ok=True)
df = pd.read_csv("results/results.csv")

for col in ["n", "time_ms", "steps", "moves", "comparisons"]:
    if df[col].dtype == object:
        df[col] = df[col].astype(str).str.replace(',', '.')
    df[col] = pd.to_numeric(df[col])

df["total_ops"] = df["steps"] + df["moves"] + df["comparisons"]

workload_configs = [
    ("W1", "default", "W1 - Random Access (10 000 get queries)"),
    ("W2", "default", "W2 - Search (1 000 contains queries)"),
    ("W3", "head", "W3 - Insert & Remove (Head: index 0)"),
    ("W3", "middle", "W3 - Insert & Remove (Middle: index n/2)"),
    ("W4", "default", "W4 - Priority Processing (MinHeap)")
]

for wl, var, title in workload_configs:
    sub = df[(df["workload"] == wl) & (df["variant"] == var)].sort_values(by="n")
    if sub.empty:
        continue

    structures = sub["structure"].unique()

    # 1. Time vs n
    plt.figure(figsize=(8, 5))
    for s in structures:
        s_data = sub[sub["structure"] == s]
        plt.plot(s_data["n"], s_data["time_ms"], marker='o', linewidth=2.5, label=s)

    plt.xscale('log')
    plt.yscale('log')
    plt.xlabel("Input Size n (log scale)")
    plt.ylabel("Execution Time (ms, log scale)")
    plt.title(f"{title} - Time vs n")
    plt.grid(True, which="both", ls="--", alpha=0.6)
    plt.legend()
    plt.tight_layout()
    plt.savefig(f"results/plots/{wl}_{var}_time.png", dpi=300)
    plt.close()

    # 2. Operations vs n
    plt.figure(figsize=(8, 5))
    for idx, s in enumerate(structures):
        s_data = sub[sub["structure"] == s]
        # Если структур две (как в W2), делаем DynamicArray пунктиром потолще, чтобы обе линии были видны
        ls = '--' if idx == 0 and len(structures) > 1 else '-'
        lw = 4 if idx == 0 and len(structures) > 1 else 2
        plt.plot(s_data["n"], s_data["total_ops"], marker='s', linestyle=ls, linewidth=lw, label=f"{s} (Total Ops)")

    plt.xscale('log')
    plt.yscale('log')
    plt.xlabel("Input Size n (log scale)")
    plt.ylabel("Physical Operations (steps + moves + comp)")
    plt.title(f"{title} - Operations vs n")
    plt.grid(True, which="both", ls="--", alpha=0.6)
    plt.legend()
    plt.tight_layout()
    plt.savefig(f"results/plots/{wl}_{var}_operations.png", dpi=300)
    plt.close()

print("Clean, visible charts generated in results/plots/!")