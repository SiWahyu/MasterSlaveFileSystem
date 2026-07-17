# Data peta Jawa Tengah (Modul Hal 19)
peta = {
    'Brebes': {'Tegal', 'Slawi'},
    'Tegal': {'Brebes', 'Pemalang', 'Slawi'},
    'Slawi': {'Brebes', 'Tegal', 'Purwokerto'},
    'Purwokerto': {'Slawi', 'Purbalingga', 'Kebumen'},
    'Cilacap': {'Purwokerto', 'Kroya'},
    'Kroya': {'Cilacap', 'Purwokerto', 'Kebumen'},
    'Kebumen': {'Kroya', 'Purwokerto', 'Purworejo'},
    'Purworejo': {'Kebumen', 'Magelang'},
    'Magelang': {'Purworejo', 'Wonosobo', 'Temanggung', 'Boyolali'},
    'Boyolali': {'Klaten', 'Solo', 'Salatiga', 'Magelang'},
    'Klaten': {'Boyolali'},
    'Solo': {'Sukoharjo', 'Sragen', 'Boyolali', 'Purwodadi'},
    'Sukoharjo': {'Solo', 'Wonogiri'},
    'Wonogiri': {'Sukoharjo'},
    'Sragen': {'Solo', 'Blora'},
    'Blora': {'Rembang', 'Sragen', 'Purwodadi'},
    'Rembang': {'Blora', 'Kudus'},
    'Kudus': {'Demak', 'Purwodadi', 'Rembang'},
    'Demak': {'Semarang', 'Purwodadi', 'Kudus'},
    'Semarang': {'Demak', 'Salatiga', 'Kendal'},
    'Kendal': {'Pekalongan', 'Temanggung', 'Semarang'},
    'Pekalongan': {'Kendal', 'Pemalang'},
    'Pemalang': {'Pekalongan', 'Tegal', 'Purbalingga'},
    'Purbalingga': {'Purwokerto', 'Banjarnegara', 'Pemalang'},
    'Banjarnegara': {'Purbalingga', 'Wonosobo'},
    'Wonosobo': {'Banjarnegara', 'Magelang', 'Temanggung'},
    'Temanggung': {'Wonosobo', 'Kendal', 'Salatiga', 'Magelang'},
    'Salatiga': {'Temanggung', 'Boyolali', 'Semarang'},
    'Purwodadi': {'Demak', 'Kudus', 'Blora', 'Solo'}
}

def dfs_lintasan(graph, mulai, goal):
    explored = []
    # DFS menggunakan Stack (LIFO)
    stack = [[mulai]]

    if mulai == goal:
        return "Awal adalah tujuan"

    while stack:
        # Perbedaan utama: pop(-1) mengambil jalur paling baru/belakang
        jalur = stack.pop(-1)
        node = jalur[-1]

        if node not in explored:
            tetangga = graph[node]
            for neighbour in tetangga:
                jalur_baru = list(jalur)
                jalur_baru.append(neighbour)
                stack.append(jalur_baru)
                
                if neighbour == goal:
                    return jalur_baru
            
            explored.append(node)
    return "Jalur tidak ditemukan"

# Menjalankan program
# Pakai .title() biar ngetik huruf kecil nggak eror lagi
awal = input("Masukkan Kota Awal: ").title()
akhir = input("Masukkan Kota Tujuan: ").title()
print("Hasil Pencarian Rute (DFS):", dfs_lintasan(peta, awal, akhir))