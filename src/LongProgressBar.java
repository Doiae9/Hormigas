import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class LongProgressBar {
        private final long total;
        private final int barWidth;
        private long startTime;
        private Predicate<Integer> percentageCondition; // Agrega esto en la clase
    private int ultimoPorcentajeMostrado = -1;
    private static final long FRECUENCIA_ACTUALIZACION = 1_000_000;
    private long ultimoMomentoActualizado = -1;

    public int getPorcentajeActual() {
        return (int)((double) ultimoProgreso / total * 100);
    }
    private long ultimoProgreso = 0; // Nuevo



    //Agregamos una barra de progresion
        public LongProgressBar(long total, int barWidth) {
            this.total = total;
            this.barWidth = barWidth;

        }

            public void setPercentageCondition(Predicate<Integer> condition) {
                this.percentageCondition = condition;
    }
        //Inicio
        public void start() {
            this.startTime = System.currentTimeMillis();
            draw(0);
        }
        //Actualizar
        public void update(long progress) {
            if (progress < 0) progress = 0;
            if (progress > total) progress = total;

            this.ultimoProgreso = progress; // 👈 Guardamos el progreso actual

            int currentPercentage = (int)((double) progress / total * 100);

            // Solo si cambió el porcentaje y es múltiplo de 10
            if (percentageCondition != null && currentPercentage % 10 == 0 && currentPercentage != ultimoPorcentajeMostrado) {
                if (percentageCondition.test(currentPercentage)) {
                    System.out.println("\n[" + currentPercentage + "%] Nuevo peso mínimo encontrado.");
                }
                ultimoPorcentajeMostrado = currentPercentage;
            }
            if (progress - ultimoMomentoActualizado >= FRECUENCIA_ACTUALIZACION || progress == total) {
                draw(progress);
                ultimoMomentoActualizado = progress;
            }
        }
    //Dibujar
    private void draw(long progress) {
        double percentage = (double) progress / total * 100;
        int filled = (int) (barWidth * percentage / 100);
        int empty = barWidth - filled;
        int currentPercentage = (int) percentage;

        StringBuilder bar = new StringBuilder();
        bar.append("\r[");
        for (int i = 0; i < filled; i++) bar.append("█");
        for (int i = 0; i < empty; i++) bar.append(" ");
        bar.append("] ");

        bar.append(String.format("%.1f%%", percentage));
        bar.append(" (").append(progress).append("/").append(total).append(")");

        if (progress > 0 && progress < total) {
            long elapsed = System.currentTimeMillis() - startTime;
            double remainingRatio = (double) (total - progress) / progress;
            long eta;

            if (remainingRatio > Long.MAX_VALUE / (double) elapsed) {
                eta = Long.MAX_VALUE;
            } else {
                eta = (long) (elapsed * remainingRatio);
            }

            if (eta < 0 || eta == Long.MAX_VALUE) {
                bar.append(" ETA: N/A");
            } else {
                bar.append(" ETA: ").append(formatTime(eta));
            }
        }

        System.out.print(bar);
    }

    //Formato de tiempo
        private String formatTime(long millis) {
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
        }
    }


