import pandas as pd
df = pd.read_csv("result.csv")
pd.set_option('display.max_columns', 200)
print(df.head(5))
