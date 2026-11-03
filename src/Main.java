void main() throws InterruptedException {
    List<Integer> numbers = new ArrayList<>();//список для хранения чисел
    Random random = new Random();
    for (int i=0;i<=1000;i++){//заполнение списка рандом числами до 100
        numbers.add(random.nextInt(100));
    }
    //ПОСЛЕДВАТЕЛЬНОЕ ВЫПОЛНЕНИЕ
    long seqStartTime = System.nanoTime();//время начала вычислений
    long seqSum=0;

    for (int number : numbers) {
        seqSum += number;//вычисляем сумму
    }
    long seqTime= System.nanoTime()-seqStartTime;//вычисляем время выполнения
    //ПАРАЛЛЕЛЬНОЕ ВЫПОЛНЕНИЕ
    long totalStartTime=System.nanoTime();//время начала
    long[]sums = new long[4];//массив для сумм каждого потока
    long[]times=new long[4];//массив для времени выполнения каждого потока
    Thread[]threads=new Thread[4];//массив четырех потоковв
    int chunk=numbers.size()/4;//колво элементов одного потока

    for(int i =0; i<4;i++) {//запуск аотоков
        final int threadNumber= i;

        threads[i]=new Thread(()->{
            long startTime = System.nanoTime();
            int from= threadNumber*chunk;//нач индекс элементов для потока
            int to=(threadNumber==3)?numbers.size():from+chunk;//конечный индекс

            for(int j=from;j<to;j++){//вычисл суммы элементов потока
                sums[threadNumber]+=numbers.get(j);
            }
            long endTime=System.nanoTime();
            times[threadNumber]=endTime-startTime;//время выполнения одного текущ потока
            System.out.println("поток №"+(threadNumber+1) +"\nсумма = "+sums[threadNumber]+"\nвремя : "+times[threadNumber]+" нс\n");
        });
        threads[i].start();//запускаем текущ поток
    }
    for(int i=0;i<4;i++){//ждем завершения всех потоков
        threads[i].join();
    }
    long totalSum=0;

    for (int i=0;i<4;i++){//складываем полученные суммы 4 потоков
        totalSum+=sums[i];
    }
    long totalEndTime = System.nanoTime();
    long parTime= totalEndTime-totalStartTime;//время выполнения 4 параллельных потоков

    System.out.println("последовательное выполнение: ");
    System.out.println("сумма = "+seqSum);
    System.out.println("время выполнения = "+seqTime+" нс");

    System.out.println("\nпараллельное выполнение: ");
    System.out.println("сумма = "+totalSum);
    System.out.println("время выполнения = "+parTime+" нс");

    System.out.println("\nСРАВНЕНИЕ: ");
    System.out.println(seqSum==totalSum?"суммы совпадают":"суммы не совпадают");
    System.out.println(parTime<seqTime?"параллельный вариант быстрее":"последовательный вариант быстрее");
}
